package com.saludlink.doctor.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.doctor.dto.CreateDoctorRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityResponse;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.doctor.dto.SubmitCredentialsRequest;
import com.saludlink.doctor.service.IDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Catalogo y gestion medica")
public class DoctorController {

    private final IDoctorService doctorService;

    @GetMapping
    @Operation(summary = "Listar medicos verificados")
    public ResponseEntity<List<DoctorResponse>> listVerified() {
        return ResponseEntity.ok(doctorService.listVerified());
    }

    @GetMapping("/specialty/{specialty}")
    @Operation(summary = "Buscar medicos por especialidad")
    public ResponseEntity<List<DoctorResponse>> bySpecialty(@PathVariable String specialty) {
        return ResponseEntity.ok(doctorService.listBySpecialty(specialty));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de medico")
    public ResponseEntity<DoctorResponse> detail(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    @PostMapping("/me/credentials")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Enviar documentos de colegiatura")
    public ResponseEntity<DoctorResponse> submitCredentials(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody SubmitCredentialsRequest request) {
        return ResponseEntity.ok(
                doctorService.submitCredentials(principal.getUser().getId(), request));
    }

    @GetMapping("/me/availability")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Listar disponibilidad del medico")
    public ResponseEntity<List<DoctorAvailabilityResponse>> listAvailability(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(doctorService.listAvailability(principal.getUser().getId()));
    }

    @PostMapping("/me/availability")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Configurar bloque de disponibilidad")
    public ResponseEntity<DoctorAvailabilityResponse> upsertAvailability(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody DoctorAvailabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.upsertAvailability(principal.getUser().getId(), request));
    }
}
