package com.saludlink.infrastructure.persistence.repository;

import com.saludlink.domain.model.entity.MedicationReminder;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationReminderRepository extends JpaRepository<MedicationReminder, Long> {

    @Query("SELECT r FROM MedicationReminder r WHERE r.medication.id = :medicationId")
    List<MedicationReminder> findByMedicationId(@Param("medicationId") Long medicationId);

    @Query(
            "SELECT r FROM MedicationReminder r WHERE r.medication.id = :medicationId AND r.reminderDate = :reminderDate")
    List<MedicationReminder> findByMedicationIdAndReminderDate(
            @Param("medicationId") Long medicationId, @Param("reminderDate") LocalDate reminderDate);

    @Query("SELECT r FROM MedicationReminder r WHERE r.medication.id = :medicationId AND r.taken = false")
    List<MedicationReminder> findByMedicationIdAndTakenFalse(@Param("medicationId") Long medicationId);
}
