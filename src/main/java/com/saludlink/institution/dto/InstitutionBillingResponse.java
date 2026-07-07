package com.saludlink.institution.dto;

import java.math.BigDecimal;
import java.util.List;

public record InstitutionBillingResponse(
        BigDecimal totalIncome,
        BigDecimal commission,
        BigDecimal pendingAmount,
        long pendingInvoiceCount,
        List<InstitutionInvoiceResponse> invoices) {}
