package com.saludlink.application.service;

import com.saludlink.application.dto.MedicationReminderRequestDTO;
import com.saludlink.application.dto.MedicationReminderResponseDTO;
import com.saludlink.infrastructure.security.CustomUserDetails;
import java.util.List;

public interface MedicationReminderService {

    List<MedicationReminderResponseDTO> listForMedication(Long medicationId, CustomUserDetails principal);

    MedicationReminderResponseDTO create(
            Long medicationId, CustomUserDetails principal, MedicationReminderRequestDTO dto);

    MedicationReminderResponseDTO markTaken(Long reminderId, CustomUserDetails principal);
}
