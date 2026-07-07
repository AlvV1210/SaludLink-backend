package com.saludlink.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreateDoctorRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank String password,
        String phone,
        @NotBlank String specialty,
        @NotBlank String licenseNumber,
        String biography,
        BigDecimal consultationFee) {}
