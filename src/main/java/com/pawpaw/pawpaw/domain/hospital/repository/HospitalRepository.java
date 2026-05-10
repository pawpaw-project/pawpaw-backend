package com.pawpaw.pawpaw.domain.hospital.repository;

import com.pawpaw.pawpaw.domain.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    List<Hospital> findByNameContaining(String name);
    Optional<Hospital> findByNameAndAddress(String name, String address);
}
