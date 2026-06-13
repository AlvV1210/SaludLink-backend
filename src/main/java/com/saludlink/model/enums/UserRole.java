package com.saludlink.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Acepta JSON en inglés o esp para el registro desde Angular. */
public enum UserRole {
    PATIENT,
    DOCTOR,
    ADMIN;

    @JsonCreator
    public static UserRole fromJson(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String v = raw.trim();
        return switch (v.toLowerCase()) {
            case "patient", "paciente" -> PATIENT;
            case "doctor", "medico", "médico" -> DOCTOR;
            case "admin", "administrador" -> ADMIN;
            default -> {
                try {
                    yield UserRole.valueOf(v.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Rol no válido: " + raw);
                }
            }
        };
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
