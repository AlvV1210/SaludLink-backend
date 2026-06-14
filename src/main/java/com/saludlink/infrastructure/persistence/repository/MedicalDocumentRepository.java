package com.saludlink.infrastructure.persistence.repository;

import com.saludlink.domain.model.entity.MedicalDocument;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {

    @Query("SELECT d FROM MedicalDocument d WHERE d.patient.id = :patientId")
    List<MedicalDocument> findByPatientId(@Param("patientId") Long patientId);

    @Query(
            "SELECT d FROM MedicalDocument d WHERE d.patient.id = :patientId AND d.documentType = :documentType")
    List<MedicalDocument> findByPatientIdAndDocumentType(
            @Param("patientId") Long patientId, @Param("documentType") String documentType);
}
