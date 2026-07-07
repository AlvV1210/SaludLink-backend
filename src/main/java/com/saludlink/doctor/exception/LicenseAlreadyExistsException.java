package com.saludlink.doctor.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class LicenseAlreadyExistsException extends BusinessRuleException {

    public LicenseAlreadyExistsException() {
        super("La matricula ya esta registrada");
    }
}
