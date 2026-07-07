package com.saludlink.auth.dto;

import com.saludlink.auth.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank String password,
        String phone,
        @NotNull UserRole role,
        String specialty,
        String licenseNumber,
        BigDecimal consultationFee) {}
