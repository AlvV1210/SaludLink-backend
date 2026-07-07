package com.saludlink.payment.dto;

import com.saludlink.payment.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long appointmentId,
        BigDecimal amount,
        String paymentMethod,
        PaymentStatus status,
        String receiptNumber,
        LocalDateTime paidAt) {}
