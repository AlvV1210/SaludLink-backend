package com.saludlink.patient.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class DependentNotFoundException extends ResourceNotFoundException {

    public DependentNotFoundException() {
        super("Perfil dependiente no encontrado");
    }
}
