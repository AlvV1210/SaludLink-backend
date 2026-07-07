package com.saludlink.medication.service;

import com.saludlink.auth.model.UserRole;
import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.exception.MedicationNotFoundException;
import com.saludlink.medication.model.Medication;
import com.saludlink.medication.repository.MedicationRepository;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.model.Patient;
import com.saludlink.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicationAuthorizationSupport {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;

    public Medication requireAccessible(Long medicationId, CustomUserDetails principal) {
        Medication medication =
                medicationRepository
                        .findById(medicationId)
                        .orElseThrow(MedicationNotFoundException::new);
        UserRole role = principal.getUser().getRole();
        if (role == UserRole.ADMIN || role == UserRole.DOCTOR) {
            return medication;
        }
        if (role != UserRole.PATIENT) {
            throw new AccessDeniedException("Rol no autorizado para este medicamento");
        }
        Long patientId =
                patientRepository
                        .findByUserId(principal.getUser().getId())
                        .map(Patient::getId)
                        .orElseThrow(PatientProfileNotFoundException::new);
        if (!medication.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("El medicamento no pertenece al paciente autenticado");
        }
        return medication;
    }
}
