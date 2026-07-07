package com.saludlink.ai.dto;

import com.saludlink.adherence.dto.AdherenceDashboardResponse;

public record AdherenceReportResponse(AdherenceDashboardResponse data, AdherenceInsight insight) {}
