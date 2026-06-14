package com.saludlink.application.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ReminderResponseDTO {

    @NotNull private Long id;

    @NotBlank private String medicationName;

    @NotNull private LocalTime scheduledTime;

    @NotNull private LocalDate reminderDate;

    private boolean taken;
}
