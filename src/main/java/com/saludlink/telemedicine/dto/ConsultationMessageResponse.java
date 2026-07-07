package com.saludlink.telemedicine.dto;

import java.time.LocalDateTime;

public record ConsultationMessageResponse(
        Long id,
        Long appointmentId,
        Long senderUserId,
        String senderName,
        String message,
        LocalDateTime sentAt) {}
