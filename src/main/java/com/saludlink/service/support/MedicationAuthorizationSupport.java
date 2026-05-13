package com.saludlink.service.support;

import com.saludlink.model.entity.Medication;
import com.saludlink.model.entity.Patient;
import com.saludlink.model.enums.UserRole;
import com.saludlink.repository.MedicationRepository;
import com.saludlink.repository.PatientRepository;
import com.saludlink.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class MedicationAuthorizationSupport {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;

    /**
     * ADMIN/DOCTOR: cualquier medicamento. PATIENT: solo si el medicamento es suyo y tiene perfil de paciente.
     */
    public Medication requireMedicationAccessible(Long medicationId, CustomUserDetails principal) {
        Medication medication =
                medicationRepository
                        .findById(medicationId)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Medicamento no encontrado: " + medicationId));
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
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST, "Perfil de paciente no encontrado"));
        if (!medication.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("El medicamento no pertenece al paciente autenticado");
        }
        return medication;
    }
}
