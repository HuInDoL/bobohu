package com.bobohu.facility.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

	Optional<Facility> findBySourceAndSourceRef(String source, String sourceRef);
}
