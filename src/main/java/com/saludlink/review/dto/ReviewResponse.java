package com.saludlink.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long appointmentId,
        Long doctorId,
        String patientName,
        int rating,
        String comment,
        LocalDateTime createdAt) {}
