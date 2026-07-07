package com.saludlink.ai.security;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.doctor.service.IDoctorService;
import com.saludlink.patient.service.IPatientService;
import com.saludlink.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiUserContext {

    private final IPatientService patientService;
    private final IDoctorService doctorService;

    public CustomUserDetails currentPrincipal() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new BusinessRuleException("Sesión no válida para el asistente de IA.");
        }
        return principal;
    }

    public Long currentPatientId() {
        return patientService.requirePatientIdForUser(currentPrincipal().getUser().getId());
    }

    public Long currentDoctorId() {
        return doctorService.requireDoctorIdForUser(currentPrincipal().getUser().getId());
    }

    public Long currentUserId() {
        return currentPrincipal().getUser().getId();
    }
}
