package com.saludlink.medicalrecord.dto;

import jakarta.validation.constraints.NotBlank;

public record DownloadClinicalRecordRequest(@NotBlank String accessCode) {}
