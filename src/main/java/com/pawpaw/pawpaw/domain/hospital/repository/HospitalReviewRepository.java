package com.pawpaw.pawpaw.domain.hospital.repository;

import com.pawpaw.pawpaw.domain.hospital.entity.HospitalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HospitalReviewRepository extends JpaRepository<HospitalReview, Long> {
    List<HospitalReview> findByHospitalIdOrderByCreatedAtDesc(Long hospitalId);
}
