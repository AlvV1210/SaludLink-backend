package com.saludlink.medication.dto;

import java.time.LocalDate;

public record CreateMedicationRequest(
        String name, String dosage, String frequency, LocalDate startDate, LocalDate endDate) {}
