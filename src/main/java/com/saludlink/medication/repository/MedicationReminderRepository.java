package com.saludlink.medication.repository;

import com.saludlink.medication.model.MedicationReminder;
import java.time.LocalDate;
import java.time.LocalTime;
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
            """
            SELECT r FROM MedicationReminder r
            WHERE r.medication.patient.id = :patientId
              AND r.reminderDate = :date
            ORDER BY r.scheduledTime
            """)
    List<MedicationReminder> findUpcomingForPatient(
            @Param("patientId") Long patientId, @Param("date") LocalDate date);

    @Query(
            """
            SELECT r FROM MedicationReminder r
            WHERE r.status = 'PENDING'
              AND r.taken = false
              AND r.reminderDate <= :date
              AND r.scheduledTime <= :time
            """)
    List<MedicationReminder> findMissedReminders(
            @Param("date") LocalDate date, @Param("time") LocalTime time);
}
