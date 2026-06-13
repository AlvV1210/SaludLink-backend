package com.saludlink.model.dto;

import com.saludlink.model.entity.MedicationReminder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationReminderResponseDTO {

    private Long id;
    private Long medicationId;
    private LocalTime scheduledTime;
    private boolean taken;
    private LocalDateTime takenAt;
    private LocalDate reminderDate;

    public static MedicationReminderResponseDTO fromEntity(MedicationReminder r) {
        return MedicationReminderResponseDTO.builder()
                .id(r.getId())
                .medicationId(r.getMedication().getId())
                .scheduledTime(r.getScheduledTime())
                .taken(r.isTaken())
                .takenAt(r.getTakenAt())
                .reminderDate(r.getReminderDate())
                .build();
    }
}
