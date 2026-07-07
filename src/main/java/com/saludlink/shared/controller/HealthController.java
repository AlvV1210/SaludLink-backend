package com.saludlink.shared.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Estado de la API")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Mensaje de bienvenida de la API")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of("message", "SaludLink API — consulta /swagger-ui.html"));
    }
}
