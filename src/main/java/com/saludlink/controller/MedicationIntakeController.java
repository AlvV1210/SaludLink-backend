package com.saludlink.controller;

import com.saludlink.model.dto.MedicationIntakeRequestDTO;
import com.saludlink.model.dto.MedicationIntakeResponseDTO;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.service.MedicationIntakeService;
import jakarta.validation.Valid;
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
public class MedicationIntakeController {

    private final MedicationIntakeService medicationIntakeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<List<MedicationIntakeResponseDTO>> list(
            @PathVariable Long medicationId, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(medicationIntakeService.listForMedication(medicationId, principal));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','DOCTOR')")
    public ResponseEntity<MedicationIntakeResponseDTO> record(
            @PathVariable Long medicationId,
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody(required = false) MedicationIntakeRequestDTO dto) {
        MedicationIntakeRequestDTO body = dto != null ? dto : new MedicationIntakeRequestDTO();
        return ResponseEntity.ok(medicationIntakeService.record(medicationId, principal, body));
    }
}
