package com.saludlink.controller;

import com.saludlink.model.dto.ApiRootResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<ApiRootResponseDTO> root() {
        return ResponseEntity.ok(
                ApiRootResponseDTO.builder()
                        .message("SaludLink API is running")
                        .health("/actuator/health")
                        .build());
    }
}
