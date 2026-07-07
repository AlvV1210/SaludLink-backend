package com.saludlink.medicalrecord.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record CreateMedicalDocumentRequest(
        @NotBlank String fileName, @NotBlank String fileUrl, String documentType) {}
