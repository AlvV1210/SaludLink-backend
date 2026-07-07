package com.saludlink.appointment.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RescheduleAppointmentRequest(@NotNull LocalDateTime appointmentDate) {}
