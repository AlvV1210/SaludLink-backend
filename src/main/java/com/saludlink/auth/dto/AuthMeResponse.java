package com.saludlink.auth.dto;

import com.saludlink.auth.model.UserRole;

public record AuthMeResponse(
        Long userId,
        String email,
        UserRole role,
        String firstName,
        String lastName,
        String phone,
        Long patientId,
        Long doctorId,
        Long institutionId) {}
