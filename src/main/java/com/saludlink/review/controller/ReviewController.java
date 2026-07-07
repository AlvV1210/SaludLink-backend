package com.saludlink.review.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.review.dto.CreateReviewRequest;
import com.saludlink.review.dto.ReviewResponse;
import com.saludlink.review.service.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Calificaciones de atencion medica")
public class ReviewController {

    private final IReviewService reviewService;

    @PostMapping("/api/reviews")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Calificar atencion recibida")
    public ResponseEntity<ReviewResponse> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(principal.getUser().getId(), request));
    }

    @GetMapping("/api/doctors/{doctorId}/reviews")
    @Operation(summary = "Listar resenas publicas del medico")
    public ResponseEntity<List<ReviewResponse>> listByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(reviewService.listByDoctor(doctorId));
    }
}
