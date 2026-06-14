package com.saludlink.infrastructure.persistence.repository;

import com.saludlink.domain.model.entity.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d WHERE d.user.id = :userId")
    Optional<Doctor> findByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN FETCH d.user WHERE d.specialty = :specialty")
    List<Doctor> findBySpecialty(@Param("specialty") String specialty);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN FETCH d.user WHERE d.verified = true")
    List<Doctor> findByVerifiedTrue();

    @Query("SELECT d FROM Doctor d JOIN FETCH d.user WHERE d.id = :id")
    Optional<Doctor> findDetailById(@Param("id") Long id);

    boolean existsByLicenseNumber(String licenseNumber);

    @Query("SELECT DISTINCT d.specialty FROM Doctor d ORDER BY d.specialty")
    List<String> findDistinctSpecialties();
}
