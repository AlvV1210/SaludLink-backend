package com.saludlink.institution.repository;

import com.saludlink.institution.model.InstitutionDoctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionDoctorRepository extends JpaRepository<InstitutionDoctor, Long> {

    @Query(
            "SELECT instDoc FROM InstitutionDoctor instDoc JOIN FETCH instDoc.doctor d JOIN FETCH d.user WHERE instDoc.institution.id = :institutionId")
    List<InstitutionDoctor> findByInstitutionId(@Param("institutionId") Long institutionId);

    Optional<InstitutionDoctor> findByInstitutionIdAndDoctorId(Long institutionId, Long doctorId);
}
