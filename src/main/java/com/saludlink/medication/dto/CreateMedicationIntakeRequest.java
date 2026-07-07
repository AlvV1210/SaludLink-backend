package com.saludlink.medication.dto;

import java.time.LocalDateTime;

public record CreateMedicationIntakeRequest(LocalDateTime takenAt, String notes) {}
