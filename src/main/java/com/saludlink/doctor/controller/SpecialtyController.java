package com.saludlink.doctor.controller;

import com.saludlink.doctor.service.IDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
@Tag(name = "Specialties", description = "Catalogo de especialidades")
public class SpecialtyController {

    private final IDoctorService doctorService;

    @GetMapping
    @Operation(summary = "Listar especialidades disponibles")
    public ResponseEntity<List<String>> list() {
        return ResponseEntity.ok(doctorService.listSpecialties());
    }
}
