package com.saludlink.medicalrecord.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ExportClinicalRecordRequest(
        @NotNull LocalDate fromDate, @NotNull LocalDate toDate) {}
