package com.saludlink.patient.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class PatientProfileNotFoundException extends ResourceNotFoundException {

    public PatientProfileNotFoundException() {
        super("Perfil de paciente no encontrado");
    }
}
