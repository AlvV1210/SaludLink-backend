package com.saludlink.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Alta de médico por administrador (usuario con rol DOCTOR + fila {@code doctors}). */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDoctorCreateDTO {

    @NotBlank private String firstName;

    @NotBlank private String lastName;

    @NotBlank @Email private String email;

    @NotBlank private String password;

    private String phone;

    @NotBlank private String specialty;

    @NotBlank private String licenseNumber;

    private String biography;

    private BigDecimal consultationFee;
}
