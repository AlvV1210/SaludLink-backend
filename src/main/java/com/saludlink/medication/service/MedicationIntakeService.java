package com.saludlink.medication.service;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.dto.CreateMedicationIntakeRequest;
import com.saludlink.medication.dto.MedicationIntakeResponse;
import com.saludlink.medication.mapper.MedicationMapper;
import com.saludlink.medication.model.MedicationIntake;
import com.saludlink.medication.repository.MedicationIntakeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicationIntakeService implements IMedicationIntakeService {

    private final MedicationIntakeRepository intakeRepository;
    private final MedicationAuthorizationSupport authorizationSupport;
    private final MedicationMapper medicationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MedicationIntakeResponse> listForMedication(
            Long medicationId, CustomUserDetails principal) {
        authorizationSupport.requireAccessible(medicationId, principal);
        return intakeRepository.findByMedicationIdOrderByTakenAtDesc(medicationId).stream()
                .map(medicationMapper::toIntakeResponse)
                .toList();
    }

    @Override
    @Transactional
    public MedicationIntakeResponse record(
            Long medicationId, CustomUserDetails principal, CreateMedicationIntakeRequest request) {
        var medication = authorizationSupport.requireAccessible(medicationId, principal);
        LocalDateTime takenAt =
                request != null && request.takenAt() != null ? request.takenAt() : LocalDateTime.now();
        String notes = request != null ? request.notes() : null;
        MedicationIntake intake =
                MedicationIntake.builder()
                        .medication(medication)
                        .takenAt(takenAt)
                        .notes(notes)
                        .build();
        return medicationMapper.toIntakeResponse(intakeRepository.save(intake));
    }
}
