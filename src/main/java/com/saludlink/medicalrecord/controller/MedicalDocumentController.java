package com.saludlink.medicalrecord.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medicalrecord.dto.CreateMedicalDocumentRequest;
import com.saludlink.medicalrecord.dto.MedicalDocumentResponse;
import com.saludlink.medicalrecord.service.IMedicalDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical-documents")
@RequiredArgsConstructor
@Tag(name = "Medical Documents", description = "Repositorio digital de documentos")
public class MedicalDocumentController {

    private final IMedicalDocumentService documentService;

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Listar documentos del paciente")
    public ResponseEntity<List<MedicalDocumentResponse>> list(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(documentService.listForUser(principal.getUser().getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Registrar documento medico")
    public ResponseEntity<MedicalDocumentResponse> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateMedicalDocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.create(principal.getUser().getId(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Eliminar documento")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        documentService.deleteForUser(principal.getUser().getId(), id);
        return ResponseEntity.noContent().build();
    }
}
