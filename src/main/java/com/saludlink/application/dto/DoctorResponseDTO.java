package com.saludlink.application.dto;

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
}
