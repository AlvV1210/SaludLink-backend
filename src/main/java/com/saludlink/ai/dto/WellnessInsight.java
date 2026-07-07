package com.saludlink.ai.dto;

import java.util.List;

public record WellnessInsight(
        String resumen,
        List<String> areasDestacadas,
        String sugerenciaAutocuidado,
        String disclaimer) {}
