package com.saludlink.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationIntakeRequestDTO {

    /** Si no se envía, se usa la fecha/hora del servidor. */
    private LocalDateTime takenAt;

    private String notes;
}
