package com.saludlink.medication.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.dto.CreateMedicationReminderRequest;
import com.saludlink.medication.dto.MedicationReminderResponse;
import com.saludlink.medication.service.IMedicationReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Medication Reminders", description = "Recordatorios de medicacion")
public class MedicationReminderController {

    private final IMedicationReminderService reminderService;

    @GetMapping("/api/medications/{medicationId}/reminders")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Listar recordatorios")
    public ResponseEntity<List<MedicationReminderResponse>> list(
            @PathVariable Long medicationId, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(reminderService.listForMedication(medicationId, principal));
    }

    @PostMapping("/api/medications/{medicationId}/reminders")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Crear recordatorio")
    public ResponseEntity<MedicationReminderResponse> create(
            @PathVariable Long medicationId,
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateMedicationReminderRequest request) {
        return ResponseEntity.ok(reminderService.create(medicationId, principal, request));
    }

    @PatchMapping("/api/medication-reminders/{id}/taken")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Marcar recordatorio como tomado")
    public ResponseEntity<MedicationReminderResponse> markTaken(
            @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(reminderService.markTaken(id, principal));
    }
}
