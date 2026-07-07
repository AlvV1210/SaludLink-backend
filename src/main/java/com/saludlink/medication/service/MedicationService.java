package com.saludlink.medication.service;

import com.saludlink.medication.dto.CreateMedicationRequest;
import com.saludlink.medication.dto.MedicationResponse;
import com.saludlink.medication.mapper.MedicationMapper;
import com.saludlink.medication.model.Medication;
import com.saludlink.medication.repository.MedicationRepository;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.repository.PatientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicationService implements IMedicationService {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;
    private final MedicationMapper medicationMapper;

    @Override
    @Transactional
    public MedicationResponse addMedication(Long patientId, CreateMedicationRequest request) {
        var patient =
                patientRepository.findById(patientId).orElseThrow(PatientProfileNotFoundException::new);
        Medication medication =
                Medication.builder()
                        .patient(patient)
                        .name(request.name())
                        .dosage(request.dosage())
                        .frequency(request.frequency())
                        .startDate(request.startDate())
                        .endDate(request.endDate())
                        .active(true)
                        .build();
        return medicationMapper.toResponse(medicationRepository.save(medication));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> listByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientProfileNotFoundException();
        }
        return medicationRepository.findByPatientId(patientId).stream()
                .map(medicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deactivate(Long medicationId) {
        Medication medication =
                medicationRepository
                        .findById(medicationId)
                        .orElseThrow(com.saludlink.medication.exception.MedicationNotFoundException::new);
        medication.setActive(false);
    }
}
