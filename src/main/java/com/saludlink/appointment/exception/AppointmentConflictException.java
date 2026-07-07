package com.saludlink.appointment.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class AppointmentConflictException extends BusinessRuleException {

    public AppointmentConflictException() {
        super("El medico ya tiene una cita en ese horario");
    }
}
