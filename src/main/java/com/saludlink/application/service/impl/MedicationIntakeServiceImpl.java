package com.saludlink.application.service.impl;

import com.saludlink.application.dto.MedicationIntakeRequestDTO;
import com.saludlink.application.dto.MedicationIntakeResponseDTO;
import com.saludlink.domain.model.entity.MedicationIntake;
import com.saludlink.infrastructure.persistence.repository.MedicationIntakeRepository;
import com.saludlink.infrastructure.security.CustomUserDetails;
import com.saludlink.application.service.MedicationIntakeService;
import com.saludlink.application.service.support.MedicationAuthorizationSupport;
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
