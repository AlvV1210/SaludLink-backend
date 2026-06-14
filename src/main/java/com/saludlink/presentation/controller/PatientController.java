package com.saludlink.presentation.controller;

import com.saludlink.application.dto.PatientProfileResponseDTO;
import com.saludlink.application.dto.PatientProfileUpdateDTO;
import com.saludlink.infrastructure.security.CustomUserDetails;
import com.saludlink.application.service.PatientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientProfileService patientProfileService;

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientProfileResponseDTO> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(patientProfileService.getProfileForUser(principal.getUser().getId()));
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientProfileResponseDTO> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody PatientProfileUpdateDTO dto) {
        return ResponseEntity.ok(
                patientProfileService.updateProfile(principal.getUser().getId(), dto));
    }
}
