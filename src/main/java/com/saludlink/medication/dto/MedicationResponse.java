package com.saludlink.medication.dto;

import java.time.LocalDate;

public record MedicationResponse(
        Long id,
        Long patientId,
        String name,
        String dosage,
        String frequency,
        LocalDate startDate,
        LocalDate endDate,
        boolean active) {}
