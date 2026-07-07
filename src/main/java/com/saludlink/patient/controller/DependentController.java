package com.saludlink.patient.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.patient.dto.CreateDependentRequest;
import com.saludlink.patient.dto.DependentResponse;
import com.saludlink.patient.service.IDependentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients/me/dependents")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')")
@Tag(name = "Dependents", description = "Perfiles dependientes del cuidador")
public class DependentController {

    private final IDependentService dependentService;

    @GetMapping
    @Operation(summary = "Listar dependientes activos")
    public ResponseEntity<List<DependentResponse>> list(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(dependentService.listForGuardian(principal.getUser().getId()));
    }

    @PostMapping
    @Operation(summary = "Agregar dependiente")
    public ResponseEntity<DependentResponse> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateDependentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dependentService.create(principal.getUser().getId(), request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de dependiente")
    public ResponseEntity<DependentResponse> getById(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        return ResponseEntity.ok(dependentService.getById(principal.getUser().getId(), id));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Desactivar dependiente")
    public ResponseEntity<Void> deactivate(
            @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
        dependentService.deactivate(principal.getUser().getId(), id);
        return ResponseEntity.noContent().build();
    }
}
