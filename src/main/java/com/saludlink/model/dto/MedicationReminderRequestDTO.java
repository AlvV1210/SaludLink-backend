package com.saludlink.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationReminderRequestDTO {

    @NotNull private LocalTime scheduledTime;

    @NotNull private LocalDate reminderDate;
}
