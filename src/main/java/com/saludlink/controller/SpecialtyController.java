package com.saludlink.controller;

import com.saludlink.repository.DoctorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final DoctorRepository doctorRepository;

    @GetMapping
    public ResponseEntity<List<String>> list() {
        return ResponseEntity.ok(doctorRepository.findDistinctSpecialties());
    }
}
