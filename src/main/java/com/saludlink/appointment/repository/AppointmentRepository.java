package com.saludlink.appointment.repository;

import com.saludlink.appointment.model.Appointment;
import com.saludlink.appointment.model.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query(
            """
            SELECT COUNT(a) > 0 FROM Appointment a
            WHERE a.doctor.id = :doctorId
              AND a.status NOT IN ('CANCELLED')
              AND a.id <> COALESCE(:excludeId, -1)
              AND a.appointmentDate = :appointmentDate
            """)
    boolean existsOverlapping(
            @Param("doctorId") Long doctorId,
            @Param("appointmentDate") LocalDateTime appointmentDate,
            @Param("excludeId") Long excludeId);

    @Query(
            """
            SELECT COUNT(a) FROM Appointment a
            JOIN InstitutionDoctor id ON id.doctor.id = a.doctor.id
            WHERE id.institution.id = :institutionId
              AND a.appointmentDate BETWEEN :start AND :end
            """)
    long countByInstitutionDoctorsAndDateBetween(
            @Param("institutionId") Long institutionId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(
            """
            SELECT COUNT(a) FROM Appointment a
            JOIN InstitutionDoctor id ON id.doctor.id = a.doctor.id
            WHERE id.institution.id = :institutionId AND a.status = :status
            """)
    long countByInstitutionDoctorsAndStatus(
            @Param("institutionId") Long institutionId, @Param("status") AppointmentStatus status);

    @Query(
            """
            SELECT COUNT(a) FROM Appointment a
            JOIN InstitutionDoctor id ON id.doctor.id = a.doctor.id
            WHERE id.institution.id = :institutionId
              AND a.status = :status
              AND a.appointmentDate BETWEEN :start AND :end
            """)
    long countByInstitutionDoctorsAndStatusBetween(
            @Param("institutionId") Long institutionId,
            @Param("status") AppointmentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(
            """
            SELECT a FROM Appointment a
            WHERE a.patient.id = :patientId
              AND a.doctor.specialty = COALESCE(:specialty, a.doctor.specialty)
              AND a.appointmentDate >= COALESCE(:from, a.appointmentDate)
              AND a.appointmentDate <= COALESCE(:to, a.appointmentDate)
            ORDER BY a.appointmentDate DESC
            """)
    List<Appointment> findPatientHistory(
            @Param("patientId") Long patientId,
            @Param("specialty") String specialty,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query(
            """
            SELECT a FROM Appointment a
            JOIN FETCH a.patient p JOIN FETCH p.user
            JOIN FETCH a.doctor d JOIN FETCH d.user
            WHERE a.id = :id
            """)
    java.util.Optional<Appointment> findDetailById(@Param("id") Long id);
}
