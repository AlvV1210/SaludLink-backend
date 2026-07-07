package com.saludlink.review.repository;

import com.saludlink.review.model.DoctorReview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorReviewRepository extends JpaRepository<DoctorReview, Long> {

    boolean existsByAppointmentId(Long appointmentId);

    Optional<DoctorReview> findByAppointmentId(Long appointmentId);

    @Query(
            """
            SELECT r FROM DoctorReview r
            JOIN FETCH r.patient p JOIN FETCH p.user
            WHERE r.doctor.id = :doctorId
            ORDER BY r.createdAt DESC
            """)
    List<DoctorReview> findByDoctorId(@Param("doctorId") Long doctorId);
}
