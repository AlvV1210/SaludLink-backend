package com.saludlink.controller;

import com.saludlink.model.dto.MedicalDocumentRequestDTO;
import com.saludlink.model.dto.MedicalDocumentResponseDTO;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.service.MedicalDocumentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class MedicalDocumentController {

    private final MedicalDocumentService medicalDocumentService;

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicalDocumentResponseDTO>> listMine(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(medicalDocumentService.listForUser(principal.getUser().getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalDocumentResponseDTO> create(
            @AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody MedicalDocumentRequestDTO dto) {
        return ResponseEntity.ok(medicalDocumentService.create(principal.getUser().getId(), dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> deleteMine(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        medicalDocumentService.deleteForUser(principal.getUser().getId(), id);
        return ResponseEntity.noContent().build();
    }
}
