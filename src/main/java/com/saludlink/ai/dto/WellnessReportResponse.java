package com.saludlink.ai.dto;

import com.saludlink.mentalhealth.dto.MentalHealthScreeningResponse;

public record WellnessReportResponse(MentalHealthScreeningResponse data, WellnessInsight insight) {}
