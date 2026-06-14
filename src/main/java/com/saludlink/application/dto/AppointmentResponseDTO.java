package com.saludlink.application.dto;

import com.saludlink.domain.model.enums.AppointmentModality;
import com.saludlink.domain.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {

    @NotNull private Long id;

    @NotBlank private String doctorName;

    @NotBlank private String specialty;

    @NotNull private LocalDateTime appointmentDate;

    @NotNull private AppointmentModality modality;

    @NotNull private AppointmentStatus status;

    private String notes;
}
