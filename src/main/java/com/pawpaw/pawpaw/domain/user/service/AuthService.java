package com.pawpaw.pawpaw.domain.user.service;

import com.pawpaw.pawpaw.domain.user.dto.*;
import com.pawpaw.pawpaw.domain.user.entity.RefreshToken;
import com.pawpaw.pawpaw.domain.user.entity.User;
import com.pawpaw.pawpaw.domain.user.repository.RefreshTokenRepository;
import com.pawpaw.pawpaw.domain.user.repository.UserRepository;
import com.pawpaw.pawpaw.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Transactional
    public void signUp(SignUpRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        return issueTokens(user);
    }

    @Transactional
    public TokenResponseDto kakaoLogin(KakaoLoginRequestDto dto) {
        String kakaoAccessToken = getKakaoAccessToken(dto.getCode(), dto.getRedirectUri());
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(kakaoAccessToken);

        Long kakaoId = userInfo.getId();
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> registerKakaoUser(kakaoId, userInfo));

        return issueTokens(user);
    }

    private String getKakaoAccessToken(String code, String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoRestApiKey);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                new HttpEntity<>(params, headers),
                KakaoTokenResponse.class
        );

        if (response.getBody() == null) {
            throw new IllegalArgumentException("카카오 토큰 발급에 실패했습니다.");
        }
        return response.getBody().getAccessToken();
    }

    private KakaoUserInfoResponse getKakaoUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);

        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KakaoUserInfoResponse.class
        );

        if (response.getBody() == null) {
            throw new IllegalArgumentException("카카오 유저 정보 조회에 실패했습니다.");
        }
        return response.getBody();
    }

    private User registerKakaoUser(Long kakaoId, KakaoUserInfoResponse userInfo) {
        String email = userInfo.getEmail() != null
                ? userInfo.getEmail()
                : "kakao_" + kakaoId + "@pawpaw.com";

        String nickname = resolveUniqueNickname(userInfo.getNickname(), kakaoId);

        return userRepository.save(User.builder()
                .email(email)
                .nickname(nickname)
                .kakaoId(kakaoId)
                .build());
    }

    private String resolveUniqueNickname(String base, Long kakaoId) {
        String nickname = (base != null && !base.isBlank()) ? base : "카카오유저";
        if (!userRepository.existsByNickname(nickname)) {
            return nickname;
        }
        return nickname + "_" + kakaoId;
    }

    private TokenResponseDto issueTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        refreshTokenRepository.findByEmail(user.getEmail())
                .ifPresentOrElse(
                        rt -> rt.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .email(user.getEmail())
                                        .token(refreshToken)
                                        .build()
                        )
                );

        return new TokenResponseDto(accessToken, refreshToken, user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public TokenResponseDto reissue(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 refreshToken입니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        RefreshToken savedToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("refreshToken이 존재하지 않습니다."));

        if (!savedToken.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("refreshToken이 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);
        savedToken.updateToken(newRefreshToken);

        return new TokenResponseDto(newAccessToken, newRefreshToken, user.getId(), user.getEmail(), user.getNickname());
    }
}
