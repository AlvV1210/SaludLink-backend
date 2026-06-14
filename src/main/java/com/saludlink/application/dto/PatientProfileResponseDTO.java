package com.saludlink.application.dto;

import com.saludlink.domain.model.enums.UserRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfileResponseDTO {

    private Long userId;
    private Long patientId;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate birthDate;
    private String bloodType;
    private String allergies;
    private String medicalHistory;
    private String insuranceInfo;
}
