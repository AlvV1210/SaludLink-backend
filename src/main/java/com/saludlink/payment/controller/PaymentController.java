package com.saludlink.payment.controller;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.payment.dto.PaymentResponse;
import com.saludlink.payment.dto.ProcessPaymentRequest;
import com.saludlink.payment.service.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Pagos de consultas en linea")
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping("/appointments/{appointmentId}")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Procesar pago de cita")
    public ResponseEntity<PaymentResponse> pay(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long appointmentId,
            @Valid @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.payAppointment(principal.getUser().getId(), appointmentId, request));
    }

    @GetMapping("/appointments/{appointmentId}")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    @Operation(summary = "Consultar pago de cita")
    public ResponseEntity<PaymentResponse> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.getByAppointmentId(appointmentId));
    }
}
