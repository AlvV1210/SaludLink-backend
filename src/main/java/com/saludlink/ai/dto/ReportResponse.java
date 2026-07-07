package com.saludlink.ai.dto;

import com.saludlink.institution.dto.InstitutionReportResponse;

public record ReportResponse(InstitutionReportResponse data, ReportInsight insight) {}
