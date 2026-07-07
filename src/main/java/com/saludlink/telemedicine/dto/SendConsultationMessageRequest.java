package com.saludlink.telemedicine.dto;

import jakarta.validation.constraints.NotBlank;

public record SendConsultationMessageRequest(@NotBlank String message) {}
