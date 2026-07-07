package com.saludlink.institution.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.institution.dto.AffiliatedDoctorRequest;
import com.saludlink.institution.dto.LinkAffiliatedDoctorRequest;
import com.saludlink.institution.dto.InstitutionBillingResponse;
import com.saludlink.institution.dto.InstitutionDashboardResponse;
import com.saludlink.institution.dto.InstitutionReportResponse;
import com.saludlink.institution.dto.InstitutionResponse;
import com.saludlink.institution.dto.RegisterInstitutionRequest;
import com.saludlink.institution.service.IInstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
@Tag(name = "Institutions", description = "Gestion institucional")
public class InstitutionController {

    private final IInstitutionService institutionService;

    @PostMapping("/register")
    @Operation(summary = "Registrar institucion de salud")
    public ResponseEntity<InstitutionResponse> register(
            @Valid @RequestBody RegisterInstitutionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionService.register(request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Perfil de la institucion autenticada")
    public ResponseEntity<InstitutionResponse> me(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(institutionService.getByAdminUserId(principal.getUser().getId()));
    }

    @GetMapping("/me/billing")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Facturacion institucional")
    public ResponseEntity<InstitutionBillingResponse> billing(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(institutionService.billing(principal.getUser().getId()));
    }

    @GetMapping("/me/dashboard")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Panel administrativo institucional")
    public ResponseEntity<InstitutionDashboardResponse> dashboard(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(institutionService.dashboard(principal.getUser().getId()));
    }

    @GetMapping("/me/reports")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Reportes de asistencia y no-shows")
    public ResponseEntity<InstitutionReportResponse> report(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(institutionService.report(principal.getUser().getId(), from, to));
    }

    @PostMapping("/me/doctors")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Agregar medico afiliado")
    public ResponseEntity<DoctorResponse> addDoctor(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody AffiliatedDoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(institutionService.addAffiliatedDoctor(principal.getUser().getId(), request));
    }

    @PostMapping("/me/doctors/link")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Vincular medico existente de SaludLink")
    public ResponseEntity<DoctorResponse> linkDoctor(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody LinkAffiliatedDoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(institutionService.linkAffiliatedDoctor(principal.getUser().getId(), request));
    }

    @GetMapping("/me/doctors")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @Operation(summary = "Listar medicos afiliados")
    public ResponseEntity<List<DoctorResponse>> listDoctors(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(institutionService.listAffiliatedDoctors(principal.getUser().getId()));
    }
}
