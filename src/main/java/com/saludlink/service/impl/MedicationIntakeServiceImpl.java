package com.saludlink.service.impl;

import com.saludlink.model.dto.MedicationIntakeRequestDTO;
import com.saludlink.model.dto.MedicationIntakeResponseDTO;
import com.saludlink.model.entity.MedicationIntake;
import com.saludlink.repository.MedicationIntakeRepository;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.service.MedicationIntakeService;
import com.saludlink.service.support.MedicationAuthorizationSupport;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicationIntakeServiceImpl implements MedicationIntakeService {

    private final MedicationIntakeRepository medicationIntakeRepository;
    private final MedicationAuthorizationSupport medicationAuthorizationSupport;

    @Override
    @Transactional(readOnly = true)
    public List<MedicationIntakeResponseDTO> listForMedication(Long medicationId, CustomUserDetails principal) {
        medicationAuthorizationSupport.requireMedicationAccessible(medicationId, principal);
        return medicationIntakeRepository.findByMedicationIdOrderByTakenAtDesc(medicationId).stream()
                .map(MedicationIntakeResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public MedicationIntakeResponseDTO record(
            Long medicationId, CustomUserDetails principal, MedicationIntakeRequestDTO dto) {
        var medication = medicationAuthorizationSupport.requireMedicationAccessible(medicationId, principal);
        LocalDateTime takenAt = dto.getTakenAt() != null ? dto.getTakenAt() : LocalDateTime.now();
        MedicationIntake intake =
                MedicationIntake.builder()
                        .medication(medication)
                        .takenAt(takenAt)
                        .notes(dto.getNotes())
                        .build();
        return MedicationIntakeResponseDTO.fromEntity(medicationIntakeRepository.save(intake));
    }
}
