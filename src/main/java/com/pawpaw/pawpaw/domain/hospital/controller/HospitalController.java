package com.pawpaw.pawpaw.domain.hospital.controller;

import com.pawpaw.pawpaw.domain.hospital.dto.HospitalResponseDto;
import com.pawpaw.pawpaw.domain.hospital.dto.HospitalReviewRequestDto;
import com.pawpaw.pawpaw.domain.hospital.dto.HospitalReviewResponseDto;
import com.pawpaw.pawpaw.domain.hospital.service.HospitalService;
import com.pawpaw.pawpaw.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/search")
    public ResponseEntity<List<HospitalResponseDto>> searchHospitals(@RequestParam String query) {
        return ResponseEntity.ok(hospitalService.searchHospitals(query));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<HospitalResponseDto>> getNearbyHospitals(
            @RequestParam Double lat, @RequestParam Double lng) {
        return ResponseEntity.ok(hospitalService.getNearbyHospitals(lat, lng));
    }

    @PostMapping("/{hospitalId}/reviews")
    public ResponseEntity<HospitalReviewResponseDto> createReview(
            @PathVariable Long hospitalId,
            @Valid @RequestBody HospitalReviewRequestDto dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(hospitalService.createReview(hospitalId, dto, user));
    }

    @GetMapping("/{hospitalId}/reviews")
    public ResponseEntity<List<HospitalReviewResponseDto>> getReviews(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(hospitalService.getReviews(hospitalId));
    }
}
