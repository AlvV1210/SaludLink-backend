package com.saludlink.model.dto;

import com.saludlink.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Datos del usuario autenticado (sin token; para GET /api/auth/me). */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMeResponseDTO {

    private Long userId;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;
    /** Presente si el usuario tiene rol paciente y perfil clínico. */
    private Long patientId;
    /** Presente si el usuario tiene rol médico y ficha profesional. */
    private Long doctorId;
}
