package com.saludlink.auth.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class EmailAlreadyExistsException extends BusinessRuleException {

    public EmailAlreadyExistsException() {
        super("El correo electronico ya esta registrado");
    }
}
