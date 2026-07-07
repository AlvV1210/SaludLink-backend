package com.saludlink.medicalrecord.repository;

import com.saludlink.medicalrecord.model.ClinicalRecordExport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalRecordExportRepository extends JpaRepository<ClinicalRecordExport, Long> {

    @Query(
            """
            SELECT e FROM ClinicalRecordExport e
            JOIN FETCH e.patient p JOIN FETCH p.user
            WHERE e.accessCode = :accessCode AND e.expiresAt > :now
            """)
    Optional<ClinicalRecordExport> findByAccessCodeAndExpiresAtAfter(
            @Param("accessCode") String accessCode, @Param("now") java.time.LocalDateTime now);
}
