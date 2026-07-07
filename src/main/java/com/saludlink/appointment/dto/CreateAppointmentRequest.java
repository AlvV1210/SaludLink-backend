package com.saludlink.appointment.dto;

import com.saludlink.appointment.model.AppointmentModality;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull Long doctorId,
        @NotNull LocalDateTime appointmentDate,
        @NotNull AppointmentModality modality,
        String notes) {}
