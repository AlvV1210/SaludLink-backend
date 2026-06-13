package com.saludlink.model.dto;

import com.saludlink.model.entity.Doctor;
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

    public static DoctorResponseDTO fromEntity(Doctor d) {
        var u = d.getUser();
        return DoctorResponseDTO.builder()
                .id(d.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .specialty(d.getSpecialty())
                .licenseNumber(d.getLicenseNumber())
                .verified(d.isVerified())
                .biography(d.getBiography())
                .consultationFee(d.getConsultationFee())
                .build();
    }
}
