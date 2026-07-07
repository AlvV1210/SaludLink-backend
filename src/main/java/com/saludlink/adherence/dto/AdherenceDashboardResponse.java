package com.saludlink.adherence.dto;

import java.math.BigDecimal;

public record AdherenceDashboardResponse(
        Long patientId,
        BigDecimal adherencePercent,
        String semaphore,
        long takenReminders,
        long totalReminders) {}
