package com.saludlink.mentalhealth.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningRequest;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningResponse;
import com.saludlink.mentalhealth.service.IMentalHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mental-health")
@RequiredArgsConstructor
@Tag(name = "Mental Health", description = "Cribado de bienestar emocional")
public class MentalHealthController {

    private final IMentalHealthService mentalHealthService;

    @PostMapping("/screenings")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Realizar test de salud mental")
    public ResponseEntity<MentalHealthScreeningResponse> submit(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody MentalHealthScreeningRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mentalHealthService.submit(principal.getUser(), request));
    }
}
