package com.saludlink.medication.controller;

import com.saludlink.medication.dto.CreateMedicationRequest;
import com.saludlink.medication.dto.MedicationResponse;
import com.saludlink.medication.service.IMedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
@Tag(name = "Medications", description = "Medicamentos del paciente")
public class MedicationController {

    private final IMedicationService medicationService;

    @PostMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Agregar medicamento")
    public ResponseEntity<MedicationResponse> add(
            @PathVariable Long patientId, @Valid @RequestBody CreateMedicationRequest request) {
        return ResponseEntity.ok(medicationService.addMedication(patientId, request));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Listar medicamentos del paciente")
    public ResponseEntity<List<MedicationResponse>> list(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicationService.listByPatient(patientId));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Desactivar medicamento")
    public ResponseEntity<Void> deactivatePut(@PathVariable Long id) {
        medicationService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    @Operation(summary = "Desactivar medicamento (alias PATCH)")
    public ResponseEntity<Void> deactivatePatch(@PathVariable Long id) {
        medicationService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
