package com.saludlink.mentalhealth.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record MentalHealthScreeningRequest(@NotEmpty List<Integer> answers) {}
