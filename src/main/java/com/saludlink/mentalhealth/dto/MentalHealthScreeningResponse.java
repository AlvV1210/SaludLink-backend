package com.saludlink.mentalhealth.dto;

public record MentalHealthScreeningResponse(
        Long id, int score, String level, String recommendation, boolean requiresReferral) {}
