package com.saludlink.review.exception;

import com.saludlink.shared.exception.BusinessRuleException;

public class ReviewAlreadyExistsException extends BusinessRuleException {

    public ReviewAlreadyExistsException() {
        super("Ya existe una resena para esta cita");
    }
}
