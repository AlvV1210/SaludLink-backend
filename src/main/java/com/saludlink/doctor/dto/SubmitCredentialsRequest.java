package com.saludlink.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record SubmitCredentialsRequest(
        @NotBlank String licenseDocumentUrl,
        String specialty,
        String licenseNumber,
        BigDecimal consultationFee) {}
