package com.saludlink.telemedicine.controller;

import com.saludlink.telemedicine.dto.EmergencyContactResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emergency")
@Tag(name = "Emergency", description = "Contactos de emergencia locales")
public class EmergencyController {

    @GetMapping("/contacts")
    @Operation(summary = "Numeros de emergencia Peru")
    public ResponseEntity<List<EmergencyContactResponse>> contacts() {
        return ResponseEntity.ok(
                List.of(
                        new EmergencyContactResponse("SAMU", "106", "Atencion medica de emergencia"),
                        new EmergencyContactResponse("Bomberos", "116", "Incendios y rescate"),
                        new EmergencyContactResponse("Policia", "105", "Seguridad ciudadana")));
    }
}
