package com.saludlink.telemedicine.dto;

import java.time.LocalDateTime;

public record TeleconsultJoinResponse(
        Long sessionId,
        Long appointmentId,
        String roomToken,
        String joinUrl,
        LocalDateTime expiresAt) {}
