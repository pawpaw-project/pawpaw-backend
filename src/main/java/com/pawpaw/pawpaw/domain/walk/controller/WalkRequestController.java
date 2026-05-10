package com.pawpaw.pawpaw.domain.walk.controller;

import com.pawpaw.pawpaw.domain.walk.dto.WalkRequestDto;
import com.pawpaw.pawpaw.domain.walk.dto.WalkRequestResponseDto;
import com.pawpaw.pawpaw.domain.walk.service.WalkRequestService;
import com.pawpaw.pawpaw.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/walk-requests")
@RequiredArgsConstructor
public class WalkRequestController {

    private final WalkRequestService walkRequestService;

    @PostMapping
    public ResponseEntity<WalkRequestResponseDto> createWalkRequest(
            @Valid @RequestBody WalkRequestDto dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(walkRequestService.createWalkRequest(dto, user));
    }

    @GetMapping
    public ResponseEntity<List<WalkRequestResponseDto>> getAllWalkRequests() {
        return ResponseEntity.ok(walkRequestService.getAllWalkRequests());
    }

    @GetMapping("/{walkRequestId}")
    public ResponseEntity<WalkRequestResponseDto> getWalkRequest(@PathVariable Long walkRequestId) {
        return ResponseEntity.ok(walkRequestService.getWalkRequest(walkRequestId));
    }

    @DeleteMapping("/{walkRequestId}")
    public ResponseEntity<Void> deleteWalkRequest(
            @PathVariable Long walkRequestId,
            @AuthenticationPrincipal User user) {
        walkRequestService.deleteWalkRequest(walkRequestId, user);
        return ResponseEntity.ok().build();
    }
}
