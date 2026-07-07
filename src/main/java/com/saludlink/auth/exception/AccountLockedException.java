package com.saludlink.auth.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class AccountLockedException extends BusinessRuleException {

    public AccountLockedException() {
        super("Cuenta bloqueada por intentos fallidos. Intenta mas tarde");
    }
}
