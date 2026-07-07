package com.saludlink.telemedicine.repository;

import com.saludlink.telemedicine.model.ConsultationMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationMessageRepository extends JpaRepository<ConsultationMessage, Long> {

    @Query(
            """
            SELECT m FROM ConsultationMessage m
            JOIN FETCH m.sender
            WHERE m.appointment.id = :appointmentId
            ORDER BY m.sentAt ASC
            """)
    List<ConsultationMessage> findByAppointmentId(@Param("appointmentId") Long appointmentId);
}
