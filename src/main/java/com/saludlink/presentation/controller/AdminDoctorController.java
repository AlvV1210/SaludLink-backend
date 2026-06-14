package com.saludlink.presentation.controller;

import com.saludlink.application.dto.AdminDoctorCreateDTO;
import com.saludlink.application.dto.DoctorResponseDTO;
import com.saludlink.application.service.AdminDoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final AdminDoctorService adminDoctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody AdminDoctorCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminDoctorService.registerDoctor(dto));
    }
}
