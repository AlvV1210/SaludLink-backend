package com.saludlink.telemedicine.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.telemedicine.dto.ConsultationMessageResponse;
import com.saludlink.telemedicine.dto.SendConsultationMessageRequest;
import com.saludlink.telemedicine.dto.TeleconsultJoinResponse;
import com.saludlink.telemedicine.service.ITelemedicineService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Telemedicine", description = "Videoconsulta y chat post-consulta")
public class TelemedicineController {

    private final ITelemedicineService telemedicineService;

    @PostMapping("/api/telemedicine/appointments/{appointmentId}/join")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    @Operation(summary = "Unirse a videoconsulta")
    public ResponseEntity<TeleconsultJoinResponse> join(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(
                telemedicineService.joinSession(principal.getUser().getId(), appointmentId));
    }

    @PostMapping("/api/telemedicine/appointments/{appointmentId}/messages")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    @Operation(summary = "Enviar mensaje post-consulta")
    public ResponseEntity<ConsultationMessageResponse> sendMessage(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long appointmentId,
            @Valid @RequestBody SendConsultationMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        telemedicineService.sendMessage(
                                principal.getUser().getId(), appointmentId, request));
    }

    @GetMapping("/api/telemedicine/appointments/{appointmentId}/messages")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    @Operation(summary = "Listar mensajes de seguimiento")
    public ResponseEntity<List<ConsultationMessageResponse>> listMessages(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(
                telemedicineService.listMessages(principal.getUser().getId(), appointmentId));
    }
}
