package com.saludlink.model.dto;

import com.saludlink.model.entity.Medication;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationResponseDTO {

    private Long id;
    private Long patientId;
    private String name;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    /** Mapeo desde entidad; usar solo dentro de la capa de servicio (sesión Hibernate abierta). */
    public static MedicationResponseDTO fromEntity(Medication m) {
        return MedicationResponseDTO.builder()
                .id(m.getId())
                .patientId(m.getPatient().getId())
                .name(m.getName())
                .dosage(m.getDosage())
                .frequency(m.getFrequency())
                .startDate(m.getStartDate())
                .endDate(m.getEndDate())
                .active(m.isActive())
                .build();
    }
}
