package com.saludlink.medication.exception;

import com.saludlink.shared.exception.ResourceNotFoundException;

public class MedicationNotFoundException extends ResourceNotFoundException {

    public MedicationNotFoundException() {
        super("Medicamento no encontrado");
    }
}
