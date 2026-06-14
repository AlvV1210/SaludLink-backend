package com.saludlink.application.service.impl;

import com.saludlink.application.dto.MedicationRequestDTO;
import com.saludlink.application.service.MedicationService;
import com.saludlink.domain.model.entity.Medication;
import com.saludlink.infrastructure.persistence.repository.MedicationRepository;
import com.saludlink.infrastructure.persistence.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
}
