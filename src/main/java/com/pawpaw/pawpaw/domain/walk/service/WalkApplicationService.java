package com.pawpaw.pawpaw.domain.walk.service;

import com.pawpaw.pawpaw.domain.walk.dto.WalkApplicationResponseDto;
import com.pawpaw.pawpaw.domain.walk.entity.WalkApplication;
import com.pawpaw.pawpaw.domain.walk.entity.WalkApplicationStatus;
import com.pawpaw.pawpaw.domain.walk.entity.WalkRequest;
import com.pawpaw.pawpaw.domain.walk.entity.WalkRequestStatus;
import com.pawpaw.pawpaw.domain.walk.repository.WalkApplicationRepository;
import com.pawpaw.pawpaw.domain.walk.repository.WalkRequestRepository;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkApplicationService {

    private final WalkApplicationRepository walkApplicationRepository;
    private final WalkRequestRepository walkRequestRepository;

    @Transactional
    public WalkApplicationResponseDto apply(Long walkRequestId, User user) {
        WalkRequest walkRequest = walkRequestRepository.findById(walkRequestId)
                .orElseThrow(() -> new IllegalArgumentException("산책 요청을 찾을 수 없습니다."));

        if (walkRequest.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 산책 요청에는 신청할 수 없습니다.");
        }

        if (walkApplicationRepository.existsByWalkRequestIdAndUserId(walkRequestId, user.getId())) {
            throw new IllegalArgumentException("이미 신청한 산책 요청입니다.");
        }

        if (!walkRequest.getStatus().equals(WalkRequestStatus.OPEN)) {
            throw new IllegalArgumentException("모집이 마감된 산책 요청입니다.");
        }

        WalkApplication walkApplication = WalkApplication.builder()
                .walkRequest(walkRequest)
                .user(user)
                .status(WalkApplicationStatus.PENDING)
                .build();

        return new WalkApplicationResponseDto(walkApplicationRepository.save(walkApplication));
    }

    @Transactional(readOnly = true)
    public List<WalkApplicationResponseDto> getApplications(Long walkRequestId) {
        return walkApplicationRepository.findByWalkRequestId(walkRequestId)
                .stream()
                .map(WalkApplicationResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public WalkApplicationResponseDto acceptApplication(Long walkRequestId, Long applicationId, User user) {
        WalkRequest walkRequest = walkRequestRepository.findById(walkRequestId)
                .orElseThrow(() -> new IllegalArgumentException("산책 요청을 찾을 수 없습니다."));

        if (!walkRequest.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수락 권한이 없습니다.");
        }

        WalkApplication walkApplication = walkApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청을 찾을 수 없습니다."));

        walkApplication.updateStatus(WalkApplicationStatus.ACCEPTED);
        walkRequest.updateStatus(WalkRequestStatus.MATCHED);

        return new WalkApplicationResponseDto(walkApplication);
    }
}
