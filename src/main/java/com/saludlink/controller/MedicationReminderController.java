package com.saludlink.controller;

import com.saludlink.model.dto.MedicationReminderRequestDTO;
import com.saludlink.model.dto.MedicationReminderResponseDTO;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.service.MedicationReminderService;
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
public class MedicationReminderController {

    private final MedicationReminderService medicationReminderService;

    @GetMapping("/api/medications/{medicationId}/reminders")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<List<MedicationReminderResponseDTO>> list(
            @PathVariable Long medicationId, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(medicationReminderService.listForMedication(medicationId, principal));
    }

    @PostMapping("/api/medications/{medicationId}/reminders")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<MedicationReminderResponseDTO> create(
            @PathVariable Long medicationId,
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody MedicationReminderRequestDTO dto) {
        return ResponseEntity.ok(medicationReminderService.create(medicationId, principal, dto));
    }

    @PatchMapping("/api/medication-reminders/{id}/taken")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<MedicationReminderResponseDTO> markTaken(
            @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(medicationReminderService.markTaken(id, principal));
    }
}
