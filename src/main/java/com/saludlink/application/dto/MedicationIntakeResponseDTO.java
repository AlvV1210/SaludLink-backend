package com.saludlink.application.dto;

import com.saludlink.domain.model.entity.MedicationIntake;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationIntakeResponseDTO {

    private Long id;
    private Long medicationId;
    private LocalDateTime takenAt;
    private String notes;
    private LocalDateTime recordedAt;

    public static MedicationIntakeResponseDTO fromEntity(MedicationIntake i) {
        return MedicationIntakeResponseDTO.builder()
                .id(i.getId())
                .medicationId(i.getMedication().getId())
                .takenAt(i.getTakenAt())
                .notes(i.getNotes())
                .recordedAt(i.getRecordedAt())
                .build();
    }
}
