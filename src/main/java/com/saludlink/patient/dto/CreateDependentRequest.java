package com.saludlink.patient.dto;

import com.saludlink.patient.model.DependentProfile.RelationshipType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateDependentRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        LocalDate birthDate,
        @NotNull RelationshipType relationship,
        String allergies,
        String medicalHistory) {}
