package com.saludlink.appointment.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class AppointmentNotFoundException extends ResourceNotFoundException {

    public AppointmentNotFoundException() {
        super("Cita no encontrada");
    }
}
