package com.saludlink.institution.dto;

import com.saludlink.institution.model.EstablishmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterInstitutionRequest(
        @NotBlank String adminFirstName,
        @NotBlank String adminLastName,
        @NotBlank String adminEmail,
        @NotBlank String adminPassword,
        String adminPhone,
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "RUC debe tener 11 digitos") String ruc,
        @NotBlank String address,
        @NotNull EstablishmentType establishmentType) {}
