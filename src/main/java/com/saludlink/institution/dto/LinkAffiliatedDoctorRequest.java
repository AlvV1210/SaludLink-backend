package com.saludlink.institution.dto;

import jakarta.validation.constraints.NotNull;

public record LinkAffiliatedDoctorRequest(@NotNull Long doctorId) {}
