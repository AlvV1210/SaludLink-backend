package com.saludlink.presentation.controller;

import com.saludlink.application.dto.MedicationRequestDTO;
import com.saludlink.application.service.MedicationService;
import com.saludlink.domain.model.entity.Medication;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<Medication> addMedication(
            @PathVariable Long patientId, @Valid @RequestBody MedicationRequestDTO dto) {
        return ResponseEntity.ok(medicationService.addMedication(patientId, dto));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<List<Medication>> listByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicationService.getMedicationsByPatient(patientId));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        medicationService.deactivateMedication(id);
        return ResponseEntity.noContent().build();
    }
}
