package com.saludlink.application.service.impl;

import com.saludlink.application.dto.MedicationReminderRequestDTO;
import com.saludlink.application.dto.MedicationReminderResponseDTO;
import com.saludlink.domain.model.entity.MedicationReminder;
import com.saludlink.infrastructure.persistence.repository.MedicationReminderRepository;
import com.saludlink.infrastructure.security.CustomUserDetails;
import com.saludlink.application.service.MedicationReminderService;
import com.saludlink.application.service.support.MedicationAuthorizationSupport;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicationReminderServiceImpl implements MedicationReminderService {

    private final MedicationReminderRepository medicationReminderRepository;
    private final MedicationAuthorizationSupport medicationAuthorizationSupport;

    @Override
    @Transactional(readOnly = true)
    public List<MedicationReminderResponseDTO> listForMedication(Long medicationId, CustomUserDetails principal) {
        medicationAuthorizationSupport.requireMedicationAccessible(medicationId, principal);
        return medicationReminderRepository.findByMedicationId(medicationId).stream()
                .sorted(
                        Comparator.comparing(MedicationReminder::getReminderDate)
                                .reversed()
                                .thenComparing(MedicationReminder::getScheduledTime))
                .map(MedicationReminderResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public MedicationReminderResponseDTO create(
            Long medicationId, CustomUserDetails principal, MedicationReminderRequestDTO dto) {
        var medication = medicationAuthorizationSupport.requireMedicationAccessible(medicationId, principal);
        MedicationReminder reminder =
                MedicationReminder.builder()
                        .medication(medication)
                        .scheduledTime(dto.getScheduledTime())
                        .reminderDate(dto.getReminderDate())
                        .taken(false)
                        .build();
        return MedicationReminderResponseDTO.fromEntity(medicationReminderRepository.save(reminder));
    }

    @Override
    public MedicationReminderResponseDTO markTaken(Long reminderId, CustomUserDetails principal) {
        MedicationReminder reminder =
                medicationReminderRepository
                        .findById(reminderId)
                        .orElseThrow(() -> new EntityNotFoundException("Recordatorio no encontrado: " + reminderId));
        medicationAuthorizationSupport.requireMedicationAccessible(reminder.getMedication().getId(), principal);
        reminder.setTaken(true);
        reminder.setTakenAt(LocalDateTime.now());
        return MedicationReminderResponseDTO.fromEntity(reminder);
    }
}
