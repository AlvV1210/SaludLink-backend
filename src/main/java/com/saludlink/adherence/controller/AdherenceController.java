package com.saludlink.adherence.controller;

import com.saludlink.adherence.dto.AdherenceDashboardResponse;
import com.saludlink.adherence.service.IAdherenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adherence")
@RequiredArgsConstructor
@Tag(name = "Adherence", description = "Tablero de adherencia terapeutica")
public class AdherenceController {

    private final IAdherenceService adherenceService;

    @GetMapping("/patients/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    @Operation(summary = "Visualizar adherencia del paciente")
    public ResponseEntity<AdherenceDashboardResponse> patientAdherence(@PathVariable Long patientId) {
        return ResponseEntity.ok(adherenceService.patientAdherence(patientId));
    }
}
