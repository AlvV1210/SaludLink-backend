package com.saludlink.institution.dto;

import com.saludlink.institution.model.EstablishmentType;
import java.time.LocalDate;

public record InstitutionReportResponse(
        LocalDate from,
        LocalDate to,
        long totalAppointments,
        long attended,
        long cancelled,
        long noShows) {}
