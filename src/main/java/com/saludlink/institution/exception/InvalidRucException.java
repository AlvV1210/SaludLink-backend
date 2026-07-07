package com.saludlink.institution.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class InvalidRucException extends BusinessRuleException {

    public InvalidRucException() {
        super("El RUC debe tener 11 digitos");
    }
}
