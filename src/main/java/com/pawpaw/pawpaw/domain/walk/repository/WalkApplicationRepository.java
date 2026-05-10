package com.pawpaw.pawpaw.domain.walk.repository;

import com.pawpaw.pawpaw.domain.walk.entity.WalkApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalkApplicationRepository extends JpaRepository<WalkApplication, Long> {
    List<WalkApplication> findByWalkRequestId(Long walkRequestId);
    Optional<WalkApplication> findByWalkRequestIdAndUserId(Long walkRequestId, Long userId);
    boolean existsByWalkRequestIdAndUserId(Long walkRequestId, Long userId);
}
