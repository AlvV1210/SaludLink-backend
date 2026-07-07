package com.saludlink.medication.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.dto.CreateMedicationIntakeRequest;
import com.saludlink.medication.dto.MedicationIntakeResponse;
import com.saludlink.medication.service.IMedicationIntakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medications/{medicationId}/intakes")
@RequiredArgsConstructor
@Tag(name = "Medication Intakes", description = "Registro de tomas")
public class MedicationIntakeController {

    private final IMedicationIntakeService intakeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Historial de tomas")
    public ResponseEntity<List<MedicationIntakeResponse>> list(
            @PathVariable Long medicationId, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(intakeService.listForMedication(medicationId, principal));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Registrar toma")
    public ResponseEntity<MedicationIntakeResponse> record(
            @PathVariable Long medicationId,
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody(required = false) CreateMedicationIntakeRequest request) {
        return ResponseEntity.ok(intakeService.record(medicationId, principal, request));
    }
}
