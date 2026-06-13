package com.saludlink.repository;

import com.saludlink.model.entity.MedicationIntake;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationIntakeRepository extends JpaRepository<MedicationIntake, Long> {

    @Query("SELECT i FROM MedicationIntake i WHERE i.medication.id = :medicationId ORDER BY i.takenAt DESC")
    List<MedicationIntake> findByMedicationIdOrderByTakenAtDesc(@Param("medicationId") Long medicationId);
}
