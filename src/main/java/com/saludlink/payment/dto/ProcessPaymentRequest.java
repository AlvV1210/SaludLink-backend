package com.saludlink.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record ProcessPaymentRequest(
        @NotNull BigDecimal amount,
        @NotBlank String paymentMethod,
        @Pattern(regexp = "\\d{4}", message = "Ultimos 4 digitos invalidos") String cardLast4) {}
