package com.saludlink.medication.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MedicationReminderResponse(
        Long id,
        Long medicationId,
        LocalTime scheduledTime,
        LocalDate reminderDate,
        boolean taken,
        LocalDateTime takenAt,
        String status) {}
