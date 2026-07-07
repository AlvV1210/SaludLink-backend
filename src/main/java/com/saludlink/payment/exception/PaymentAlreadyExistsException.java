package com.saludlink.payment.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class PaymentAlreadyExistsException extends BusinessRuleException {

    public PaymentAlreadyExistsException() {
        super("La cita ya tiene un pago registrado");
    }
}
