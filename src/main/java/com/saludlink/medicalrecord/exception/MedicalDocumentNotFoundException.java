package com.saludlink.medicalrecord.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class MedicalDocumentNotFoundException extends ResourceNotFoundException {

    public MedicalDocumentNotFoundException() {
        super("Documento no encontrado");
    }
}
