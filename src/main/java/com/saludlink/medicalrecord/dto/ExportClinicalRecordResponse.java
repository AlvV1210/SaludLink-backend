package com.saludlink.medicalrecord.dto;

import java.time.LocalDateTime;

public record ExportClinicalRecordResponse(
        Long exportId, String fileName, String accessCode, LocalDateTime expiresAt) {}
