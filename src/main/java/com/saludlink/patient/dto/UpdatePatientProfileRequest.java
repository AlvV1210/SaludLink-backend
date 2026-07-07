package com.saludlink.patient.dto;

import java.time.LocalDate;

public record UpdatePatientProfileRequest(
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        String bloodType,
        String allergies,
        String medicalHistory,
        String insuranceInfo) {}
