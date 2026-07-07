package com.saludlink.institution.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class RucAlreadyExistsException extends BusinessRuleException {

    public RucAlreadyExistsException() {
        super("El RUC ya esta registrado");
    }
}
