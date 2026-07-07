package com.saludlink.institution.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class InstitutionNotFoundException extends ResourceNotFoundException {

    public InstitutionNotFoundException() {
        super("Institucion no encontrada");
    }
}
