package com.saludlink.institution.dto;

import com.saludlink.institution.model.EstablishmentType;
import java.time.LocalDateTime;

public record InstitutionResponse(
        Long id,
        String name,
        String ruc,
        String address,
        EstablishmentType establishmentType,
        Long adminUserId,
        LocalDateTime createdAt) {}
