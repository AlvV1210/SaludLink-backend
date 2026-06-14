package com.saludlink.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationRequestDTO {

    @NotBlank private String name;

    private String dosage;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;
}
