package com.saludlink.appointment.controller;

import com.saludlink.appointment.dto.AppointmentResponse;
import com.saludlink.appointment.dto.CreateAppointmentRequest;
import com.saludlink.appointment.dto.RescheduleAppointmentRequest;
import com.saludlink.appointment.dto.UpdateAppointmentStatusRequest;
import com.saludlink.appointment.service.IAppointmentService;
import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.patient.service.IPatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Gestion de citas medicas")
public class AppointmentController {

    private final IAppointmentService appointmentService;
    private final IPatientService patientService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Reservar cita medica")
    public ResponseEntity<AppointmentResponse> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateAppointmentRequest request) {
        Long patientId = patientService.requirePatientIdForUser(principal.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.create(patientId, request));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    @Operation(summary = "Listar citas por paciente")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.listByPatient(patientId));
    }

    @GetMapping("/patient/{patientId}/history")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    @Operation(summary = "Historial de citas con filtros")
    public ResponseEntity<List<AppointmentResponse>> history(
            @PathVariable Long patientId,
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime to) {
        return ResponseEntity.ok(appointmentService.patientHistory(patientId, specialty, from, to));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    @Operation(summary = "Listar citas por medico")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.listByDoctor(doctorId));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    @Operation(summary = "Cancelar cita")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    @Operation(summary = "Reprogramar cita")
    public ResponseEntity<AppointmentResponse> reschedule(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody RescheduleAppointmentRequest request) {
        return ResponseEntity.ok(
                appointmentService.reschedule(id, principal.getUser().getId(), request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Actualizar estado de cita")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateAppointmentStatusRequest body) {
        appointmentService.updateStatus(id, body);
        return ResponseEntity.noContent().build();
    }
}
