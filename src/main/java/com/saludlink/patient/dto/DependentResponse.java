package com.saludlink.patient.dto;

import com.saludlink.patient.model.DependentProfile.RelationshipType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DependentResponse(
        Long id,
        Long guardianPatientId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        RelationshipType relationship,
        String allergies,
        String medicalHistory,
        boolean active,
        LocalDateTime createdAt) {}
