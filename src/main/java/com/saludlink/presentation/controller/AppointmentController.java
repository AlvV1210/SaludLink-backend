package com.saludlink.presentation.controller;

import com.saludlink.application.dto.AppointmentRequestDTO;
import com.saludlink.application.dto.AppointmentResponseDTO;
import com.saludlink.application.dto.AppointmentStatusUpdateDTO;
import com.saludlink.application.service.AppointmentService;
import com.saludlink.domain.model.entity.Patient;
import com.saludlink.infrastructure.persistence.repository.PatientRepository;
import com.saludlink.infrastructure.security.CustomUserDetails;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDTO> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody AppointmentRequestDTO dto) {
        Long patientId =
                patientRepository
                        .findByUserId(principal.getUser().getId())
                        .map(Patient::getId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST, "Perfil de paciente no encontrado"));
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
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
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
