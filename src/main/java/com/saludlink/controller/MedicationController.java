package com.saludlink.controller;

import com.saludlink.model.dto.MedicationRequestDTO;
import com.saludlink.model.dto.MedicationResponseDTO;
import com.saludlink.model.entity.Patient;
import com.saludlink.repository.PatientRepository;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.service.MedicationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Medicamentos del paciente. {@code PUT} y {@code PATCH} en {@code /{id}/deactivate} realizan la misma operación
 * (baja lógica) para alinear con distintas expectativas del cliente HTTP / front; conviene documentar una como
 * canónica en OpenAPI y tratar la otra como alias.
 */
@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;
    private final PatientRepository patientRepository;

    private Long requirePatientId(CustomUserDetails principal) {
        return patientRepository
                .findByUserId(principal.getUser().getId())
                .map(Patient::getId)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Perfil de paciente no encontrado"));
    }

    private boolean isPatient(CustomUserDetails principal) {
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_PATIENT"::equals);
    }

    /** Contrato frontend: listado del paciente autenticado. */
    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicationResponseDTO>> listMine(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(medicationService.getMedicationsByPatient(requirePatientId(principal)));
    }

    /** Contrato frontend: alta para el paciente autenticado. */
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicationResponseDTO> addMine(
            @AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody MedicationRequestDTO dto) {
        return ResponseEntity.ok(medicationService.addMedication(requirePatientId(principal), dto));
    }

    @PostMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<MedicationResponseDTO> addMedication(
            @PathVariable Long patientId, @Valid @RequestBody MedicationRequestDTO dto) {
        return ResponseEntity.ok(medicationService.addMedication(patientId, dto));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<List<MedicationResponseDTO>> listByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicationService.getMedicationsByPatient(patientId));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<Void> deactivatePut(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        if (isPatient(principal)) {
            medicationService.deactivateMedicationForPatient(id, requirePatientId(principal));
        } else {
            medicationService.deactivateMedication(id);
        }
        return ResponseEntity.noContent().build();
    }

    /** Contrato frontend: PATCH desactivar medicamento. */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<Void> deactivatePatch(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        if (isPatient(principal)) {
            medicationService.deactivateMedicationForPatient(id, requirePatientId(principal));
        } else {
            medicationService.deactivateMedication(id);
        }
        return ResponseEntity.noContent().build();
    }
}
