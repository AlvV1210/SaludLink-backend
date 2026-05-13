package com.saludlink.controller;

import com.saludlink.model.dto.DoctorResponseDTO;
import com.saludlink.model.entity.Doctor;
import com.saludlink.repository.DoctorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorRepository doctorRepository;

    private static DoctorResponseDTO toDto(Doctor d) {
        var u = d.getUser();
        return DoctorResponseDTO.builder()
                .id(d.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .specialty(d.getSpecialty())
                .licenseNumber(d.getLicenseNumber())
                .verified(d.isVerified())
                .biography(d.getBiography())
                .consultationFee(d.getConsultationFee())
                .build();
    }

    /**
     * Lista médicos verificados; si {@code specialty} está presente filtra (contrato frontend:
     * {@code GET /api/doctors?specialty=...}).
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> list(
            @RequestParam(required = false) String specialty) {
        List<DoctorResponseDTO> list;
        if (specialty != null && !specialty.isBlank()) {
            list = doctorRepository.findBySpecialty(specialty).stream().map(DoctorController::toDto).toList();
        } else {
            list = doctorRepository.findByVerifiedTrue().stream().map(DoctorController::toDto).toList();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<DoctorResponseDTO>> bySpecialtyPath(@PathVariable String specialty) {
        List<DoctorResponseDTO> list =
                doctorRepository.findBySpecialty(specialty).stream().map(DoctorController::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> detail(@PathVariable Long id) {
        Doctor doctor =
                doctorRepository
                        .findDetailById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado"));
        return ResponseEntity.ok(toDto(doctor));
    }
}
