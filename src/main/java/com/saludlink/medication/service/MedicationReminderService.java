package com.saludlink.medication.service;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.dto.CreateMedicationReminderRequest;
import com.saludlink.medication.dto.MedicationReminderResponse;
import com.saludlink.medication.exception.MedicationNotFoundException;
import com.saludlink.medication.mapper.MedicationMapper;
import com.saludlink.medication.model.MedicationReminder;
import com.saludlink.medication.repository.MedicationReminderRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicationReminderService implements IMedicationReminderService {

    private final MedicationReminderRepository reminderRepository;
    private final MedicationAuthorizationSupport authorizationSupport;
    private final MedicationMapper medicationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MedicationReminderResponse> listForMedication(
            Long medicationId, CustomUserDetails principal) {
        authorizationSupport.requireAccessible(medicationId, principal);
        return reminderRepository.findByMedicationId(medicationId).stream()
                .sorted(
                        Comparator.comparing(MedicationReminder::getReminderDate)
                                .reversed()
                                .thenComparing(MedicationReminder::getScheduledTime))
                .map(medicationMapper::toReminderResponse)
                .toList();
    }

    @Override
    @Transactional
    public MedicationReminderResponse create(
            Long medicationId, CustomUserDetails principal, CreateMedicationReminderRequest request) {
        var medication = authorizationSupport.requireAccessible(medicationId, principal);
        MedicationReminder reminder =
                MedicationReminder.builder()
                        .medication(medication)
                        .scheduledTime(request.scheduledTime())
                        .reminderDate(request.reminderDate())
                        .taken(false)
                        .status("PENDING")
                        .build();
        return medicationMapper.toReminderResponse(reminderRepository.save(reminder));
    }

    @Override
    @Transactional
    public MedicationReminderResponse markTaken(Long reminderId, CustomUserDetails principal) {
        MedicationReminder reminder =
                reminderRepository
                        .findById(reminderId)
                        .orElseThrow(MedicationNotFoundException::new);
        authorizationSupport.requireAccessible(reminder.getMedication().getId(), principal);
        reminder.setTaken(true);
        reminder.setTakenAt(LocalDateTime.now());
        reminder.setStatus("TAKEN");
        return medicationMapper.toReminderResponse(reminder);
    }
}
