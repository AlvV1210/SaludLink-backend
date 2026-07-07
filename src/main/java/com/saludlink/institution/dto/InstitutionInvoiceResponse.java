package com.saludlink.institution.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InstitutionInvoiceResponse(
        String concept,
        String reference,
        BigDecimal amount,
        LocalDateTime paidAt,
        String status) {}
