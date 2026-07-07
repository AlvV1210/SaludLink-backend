package com.saludlink.telemedicine.repository;

import com.saludlink.telemedicine.model.TeleconsultSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleconsultSessionRepository extends JpaRepository<TeleconsultSession, Long> {

    Optional<TeleconsultSession> findByAppointmentId(Long appointmentId);
}
