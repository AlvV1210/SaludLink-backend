package com.saludlink.medication.dto;

import java.time.LocalDateTime;

public record MedicationIntakeResponse(Long id, Long medicationId, LocalDateTime takenAt, String notes) {}
