package com.saludlink.payment.repository;

import com.saludlink.payment.model.AppointmentPayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentPaymentRepository extends JpaRepository<AppointmentPayment, Long> {

    Optional<AppointmentPayment> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);

    @Query(
            """
            SELECT p FROM AppointmentPayment p
            JOIN FETCH p.appointment a
            JOIN InstitutionDoctor id ON id.doctor.id = a.doctor.id
            WHERE id.institution.id = :institutionId
            ORDER BY p.paidAt DESC
            """)
    List<AppointmentPayment> findByInstitutionId(@Param("institutionId") Long institutionId);
}
