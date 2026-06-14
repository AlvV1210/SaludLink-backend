package com.saludlink.infrastructure.persistence.repository;

import com.saludlink.domain.model.entity.Medication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    @Query("SELECT m FROM Medication m WHERE m.patient.id = :patientId")
    List<Medication> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT m FROM Medication m WHERE m.patient.id = :patientId AND m.active = true")
    List<Medication> findByPatientIdAndActiveTrue(@Param("patientId") Long patientId);
}
