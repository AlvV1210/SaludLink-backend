package com.saludlink.application.dto;

import com.saludlink.domain.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentStatusUpdateDTO {

    @NotNull private AppointmentStatus status;
}
