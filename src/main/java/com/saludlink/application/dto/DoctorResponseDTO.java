package com.saludlink.application.dto;

import com.saludlink.domain.model.entity.Doctor;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialty;
    private String licenseNumber;
    private boolean verified;
    private String biography;
    private BigDecimal consultationFee;

    public static DoctorResponseDTO fromEntity(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .firstName(doctor.getUser().getFirstName())
                .lastName(doctor.getUser().getLastName())
                .email(doctor.getUser().getEmail())
                .specialty(doctor.getSpecialty())
                .licenseNumber(doctor.getLicenseNumber())
                .verified(doctor.isVerified())
                .biography(doctor.getBiography())
                .consultationFee(doctor.getConsultationFee())
                .build();
    }
}
