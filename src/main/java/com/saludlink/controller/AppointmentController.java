package com.saludlink.controller;

import com.saludlink.model.dto.AppointmentRequestDTO;
import com.saludlink.model.dto.AppointmentResponseDTO;
import com.saludlink.model.dto.AppointmentStatusUpdateDTO;
import com.saludlink.model.entity.Patient;
import com.saludlink.repository.PatientRepository;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.service.AppointmentService;
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

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
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

    private boolean isAdmin(CustomUserDetails principal) {
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    /** Contrato frontend: listado del paciente autenticado. Usa desde el SPA*/
    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> listMine(@AuthenticationPrincipal CustomUserDetails principal) {
        Long patientId = requirePatientId(principal);
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> create(
            @AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody AppointmentRequestDTO dto) {
        Long patientId = requirePatientId(principal);
        return ResponseEntity.ok(appointmentService.createAppointment(patientId, dto));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public ResponseEntity<Void> cancelPut(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        if (isAdmin(principal)) {
            appointmentService.cancelAppointment(id);
        } else {
            appointmentService.cancelAppointmentForPatient(id, requirePatientId(principal));
        }
        return ResponseEntity.noContent().build();
    }

    /** Contrato frontend: PATCH cancelar cita (paciente dueño). */
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> cancelPatch(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        appointmentService.cancelAppointmentForPatient(id, requirePatientId(principal));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id, @Valid @RequestBody AppointmentStatusUpdateDTO body) {
        appointmentService.updateAppointmentStatus(id, body.getStatus());
        return ResponseEntity.noContent().build();
    }
}
