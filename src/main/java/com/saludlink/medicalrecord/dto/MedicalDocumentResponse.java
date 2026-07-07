package com.saludlink.medicalrecord.dto;

import java.time.LocalDateTime;

public record MedicalDocumentResponse(
        Long id,
        String fileName,
        String fileUrl,
        String documentType,
        LocalDateTime uploadedAt) {}
