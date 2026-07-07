package com.saludlink.appointment.dto;

import com.saludlink.appointment.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequest(@NotNull AppointmentStatus status, String notes) {}
