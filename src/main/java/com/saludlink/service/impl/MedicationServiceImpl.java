package com.saludlink.service.impl;

import com.saludlink.model.dto.MedicationRequestDTO;
import com.saludlink.model.entity.Medication;
import com.saludlink.repository.MedicationRepository;
import com.saludlink.repository.PatientRepository;
import com.saludlink.service.MedicationService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;

    @Override
    public Medication addMedication(Long patientId, MedicationRequestDTO dto) {
        var patient =
                patientRepository
                        .findById(patientId)
                        .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + patientId));
        Medication medication =
                Medication.builder()
                        .patient(patient)
                        .name(dto.getName())
                        .dosage(dto.getDosage())
                        .frequency(dto.getFrequency())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .active(true)
                        .build();
        return medicationRepository.save(medication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medication> getMedicationsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new EntityNotFoundException("Paciente no encontrado: " + patientId);
        }
        return medicationRepository.findByPatientId(patientId);
    }

    @Override
    public void deactivateMedication(Long medicationId) {
        Medication medication =
                medicationRepository
                        .findById(medicationId)
                        .orElseThrow(() -> new EntityNotFoundException("Medicamento no encontrado: " + medicationId));
        medication.setActive(false);
    }

    @Override
    public void deactivateMedicationForPatient(Long medicationId, Long patientId) {
        Medication medication =
                medicationRepository
                        .findById(medicationId)
                        .orElseThrow(() -> new EntityNotFoundException("Medicamento no encontrado: " + medicationId));
        if (!medication.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("El medicamento no pertenece al paciente autenticado");
        }
        medication.setActive(false);
    }
}
