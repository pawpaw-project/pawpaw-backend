package com.pawpaw.pawpaw.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @Setter
        public static class Profile {
            private String nickname;
        }
    }

    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.getEmail() : null;
    }

    public String getNickname() {
        return kakaoAccount != null && kakaoAccount.getProfile() != null
                ? kakaoAccount.getProfile().getNickname() : null;
    }
}
