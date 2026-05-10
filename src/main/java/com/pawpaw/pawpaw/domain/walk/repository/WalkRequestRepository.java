package com.pawpaw.pawpaw.domain.walk.repository;

import com.pawpaw.pawpaw.domain.walk.entity.WalkRequest;
import com.pawpaw.pawpaw.domain.walk.entity.WalkRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkRequestRepository extends JpaRepository<WalkRequest, Long> {
    List<WalkRequest> findAllByOrderByCreatedAtDesc();
    List<WalkRequest> findByStatusOrderByCreatedAtDesc(WalkRequestStatus status);
    List<WalkRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
}
