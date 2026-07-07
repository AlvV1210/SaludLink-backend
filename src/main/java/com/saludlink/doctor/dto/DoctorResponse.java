package com.saludlink.doctor.dto;

import java.math.BigDecimal;

public record DoctorResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String specialty,
        String licenseNumber,
        boolean verified,
        String biography,
        BigDecimal consultationFee) {}
