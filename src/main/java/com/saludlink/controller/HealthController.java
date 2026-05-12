package com.saludlink.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of("message", "SaludLink API is running", "health", "/actuator/health");
    }
}
