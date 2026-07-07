package com.saludlink.doctor.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class DoctorNotFoundException extends ResourceNotFoundException {

    public DoctorNotFoundException() {
        super("Medico no encontrado");
    }
}
