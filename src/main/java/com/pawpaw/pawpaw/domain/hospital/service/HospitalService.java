package com.pawpaw.pawpaw.domain.hospital.service;

import com.pawpaw.pawpaw.domain.hospital.dto.HospitalResponseDto;
import com.pawpaw.pawpaw.domain.hospital.dto.HospitalReviewRequestDto;
import com.pawpaw.pawpaw.domain.hospital.dto.HospitalReviewResponseDto;
import com.pawpaw.pawpaw.domain.hospital.dto.KakaoSearchResponse;
import com.pawpaw.pawpaw.domain.hospital.entity.Hospital;
import com.pawpaw.pawpaw.domain.hospital.entity.HospitalReview;
import com.pawpaw.pawpaw.domain.hospital.repository.HospitalRepository;
import com.pawpaw.pawpaw.domain.hospital.repository.HospitalReviewRepository;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalReviewRepository hospitalReviewRepository;
    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    public List<HospitalResponseDto> searchHospitals(String query) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query + "+동물병원&size=10";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<KakaoSearchResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoSearchResponse.class);

        if (response.getBody() == null || response.getBody().getDocuments() == null) {
            return new ArrayList<>();
        }

        return response.getBody().getDocuments().stream()
                .map(doc -> {
                    String address = doc.getRoadAddressName() != null && !doc.getRoadAddressName().isEmpty()
                            ? doc.getRoadAddressName() : doc.getAddressName();
                    Hospital hospital = Hospital.builder()
                            .name(doc.getPlaceName())
                            .address(address)
                            .lat(Double.parseDouble(doc.getY()))
                            .lng(Double.parseDouble(doc.getX()))
                            .phone(doc.getPhone())
                            .build();
                    return new HospitalResponseDto(hospitalRepository.save(hospital));
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HospitalResponseDto> getNearbyHospitals(Double lat, Double lng) {
        return hospitalRepository.findAll()
                .stream()
                .filter(h -> getDistance(lat, lng, h.getLat(), h.getLng()) <= 5.0)
                .map(HospitalResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public HospitalReviewResponseDto createReview(Long hospitalId, HospitalReviewRequestDto dto, User user) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다."));

        HospitalReview review = HospitalReview.builder()
                .hospital(hospital)
                .user(user)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        return new HospitalReviewResponseDto(hospitalReviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<HospitalReviewResponseDto> getReviews(Long hospitalId) {
        return hospitalReviewRepository.findByHospitalIdOrderByCreatedAtDesc(hospitalId)
                .stream()
                .map(HospitalReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
