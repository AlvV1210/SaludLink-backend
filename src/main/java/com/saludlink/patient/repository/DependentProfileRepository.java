package com.saludlink.patient.repository;

import com.saludlink.patient.model.DependentProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependentProfileRepository extends JpaRepository<DependentProfile, Long> {

    List<DependentProfile> findByGuardianIdAndActiveTrue(Long guardianPatientId);

    Optional<DependentProfile> findByIdAndGuardianId(Long id, Long guardianPatientId);
}
