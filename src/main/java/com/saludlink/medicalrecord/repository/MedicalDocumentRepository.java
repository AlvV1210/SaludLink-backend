package com.saludlink.medicalrecord.repository;

import com.saludlink.medicalrecord.model.MedicalDocument;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {

    @Query(
            "SELECT d FROM MedicalDocument d WHERE d.patient.id = :patientId ORDER BY d.uploadedAt DESC")
    List<MedicalDocument> findByPatientId(@Param("patientId") Long patientId);
}
