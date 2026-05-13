package com.saludlink.model.dto;

import com.saludlink.model.enums.AppointmentModality;
import com.saludlink.model.enums.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Respuesta de citas (solo salida HTTP; sin anotaciones de validación de entrada). */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {

    private Long id;
    private String doctorName;
    private String specialty;
    private LocalDateTime appointmentDate;
    private AppointmentModality modality;
    private AppointmentStatus status;
    private String notes;
}
