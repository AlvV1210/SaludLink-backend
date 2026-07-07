package com.saludlink.medicalrecord.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medicalrecord.dto.ExportClinicalRecordRequest;
import com.saludlink.medicalrecord.dto.ExportClinicalRecordResponse;
import com.saludlink.medicalrecord.service.IClinicalRecordExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "Clinical Record Export", description = "Exportacion segura de historial clinico")
public class ClinicalRecordExportController {

    private final IClinicalRecordExportService exportService;

    @PostMapping("/export")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Generar exportacion con codigo de acceso temporal")
    public ResponseEntity<ExportClinicalRecordResponse> export(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody ExportClinicalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exportService.createExport(principal.getUser().getId(), request));
    }

    @GetMapping("/export/{accessCode}/download")
    @Operation(summary = "Descargar PDF con codigo de acceso")
    public ResponseEntity<byte[]> download(@PathVariable String accessCode) {
        byte[] pdf = exportService.downloadByAccessCode(accessCode);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historial-clinico.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
