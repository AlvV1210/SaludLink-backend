package com.saludlink.patient.dto;

import com.saludlink.auth.model.UserRole;
import java.time.LocalDate;

public record PatientProfileResponse(
        Long userId,
        Long patientId,
        String email,
        UserRole role,
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        String bloodType,
        String allergies,
        String medicalHistory,
        String insuranceInfo,
        String alertSound,
        String alertFrequency) {}
