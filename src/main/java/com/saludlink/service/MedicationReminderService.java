package com.saludlink.service;

import com.saludlink.model.dto.MedicationReminderRequestDTO;
import com.saludlink.model.dto.MedicationReminderResponseDTO;
import com.saludlink.security.CustomUserDetails;
import java.util.List;

public interface MedicationReminderService {

    List<MedicationReminderResponseDTO> listForMedication(Long medicationId, CustomUserDetails principal);

    MedicationReminderResponseDTO create(
            Long medicationId, CustomUserDetails principal, MedicationReminderRequestDTO dto);

    MedicationReminderResponseDTO markTaken(Long reminderId, CustomUserDetails principal);
}
