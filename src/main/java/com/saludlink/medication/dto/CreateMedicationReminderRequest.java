package com.saludlink.medication.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateMedicationReminderRequest(LocalTime scheduledTime, LocalDate reminderDate) {}
