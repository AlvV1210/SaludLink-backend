package com.saludlink.appointment.dto;

import com.saludlink.appointment.model.AppointmentModality;
import com.saludlink.appointment.model.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        String doctorName,
        String specialty,
        LocalDateTime appointmentDate,
        AppointmentModality modality,
        AppointmentStatus status,
        String notes) {}
