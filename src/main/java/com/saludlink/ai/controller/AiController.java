package com.saludlink.ai.controller;

import com.saludlink.ai.dto.AdherenceReportResponse;
import com.saludlink.ai.dto.ChatRequest;
import com.saludlink.ai.dto.ChatResponse;
import com.saludlink.ai.dto.ReportResponse;
import com.saludlink.ai.dto.SupportAnswer;
import com.saludlink.ai.dto.WellnessReportResponse;
import com.saludlink.ai.service.AiService;
import com.saludlink.ai.service.SupportService;
import com.saludlink.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Asistente IA SaludLink (tool calling, structured output y RAG)")
public class AiController {

    private final AiService aiService;
    private final SupportService supportService;

    @Operation(summary = "Paciente: reservar cita desde texto (tool calling)")
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/appointment")
    public ChatResponse appointment(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(aiService.appointment(request.message()));
    }

    @Operation(summary = "Admin institución: reporte de asistencia con resumen IA")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @GetMapping("/report")
    public ReportResponse institutionReport(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return aiService.institutionReport(principal.getUser().getId(), from, to);
    }

    @Operation(summary = "Paciente: gestionar medicación desde texto (tool calling)")
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/medication")
    public ChatResponse medication(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(aiService.medication(request.message()));
    }

    @Operation(summary = "Médico: reporte de adherencia con insight IA")
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/adherence/report")
    public AdherenceReportResponse adherenceReport(@RequestParam Long patientId) {
        return aiService.adherenceReport(patientId);
    }

    @Operation(summary = "Médico: consultar agenda desde texto (tool calling)")
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/schedule")
    public ChatResponse schedule(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(aiService.schedule(request.message()));
    }

    @Operation(summary = "Paciente: insight de bienestar tras cribado de salud mental")
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/wellness/insight")
    public WellnessReportResponse wellnessInsight(@AuthenticationPrincipal CustomUserDetails principal) {
        return aiService.wellnessInsight(principal.getUser());
    }

    @Operation(summary = "Usuario autenticado: soporte con RAG")
    @PostMapping("/support/ask")
    public SupportAnswer supportAsk(@Valid @RequestBody ChatRequest request) {
        return supportService.ask(request.message());
    }

    @Operation(summary = "Admin institución: ingesta PDFs al vector store")
    @PreAuthorize("hasRole('INSTITUTION_ADMIN')")
    @PostMapping("/support/ingest")
    public Map<String, Object> supportIngest() throws IOException {
        return Map.of("chunksIngested", supportService.ingest());
    }
}
