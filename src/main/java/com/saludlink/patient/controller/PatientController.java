package com.saludlink.patient.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.patient.dto.NotificationPreferencesRequest;
import com.saludlink.patient.dto.PatientProfileResponse;
import com.saludlink.patient.dto.UpdatePatientProfileRequest;
import com.saludlink.patient.service.IPatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Perfil de salud del paciente")
public class PatientController {

    private final IPatientService patientService;

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Obtener perfil de salud")
    public ResponseEntity<PatientProfileResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(patientService.getProfile(principal.getUser().getId()));
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Actualizar perfil de salud")
    public ResponseEntity<PatientProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody UpdatePatientProfileRequest request) {
        return ResponseEntity.ok(
                patientService.updateProfile(principal.getUser().getId(), request));
    }

    @PutMapping("/me/notification-preferences")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Personalizar alertas de salud")
    public ResponseEntity<PatientProfileResponse> updateNotificationPreferences(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody NotificationPreferencesRequest request) {
        return ResponseEntity.ok(
                patientService.updateNotificationPreferences(principal.getUser().getId(), request));
    }
}
