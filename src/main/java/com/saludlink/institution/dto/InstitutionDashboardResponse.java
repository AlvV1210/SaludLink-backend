package com.saludlink.institution.dto;

import java.math.BigDecimal;

public record InstitutionDashboardResponse(
        long todayAppointments,
        BigDecimal medicalOccupancyRate,
        long noShowAlerts,
        BigDecimal averageAdherencePercent) {}
