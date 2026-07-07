package com.saludlink.medication.service;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.dto.CreateMedicationReminderRequest;
import com.saludlink.medication.dto.MedicationReminderResponse;
import java.util.List;

public interface IMedicationReminderService {

    List<MedicationReminderResponse> listForMedication(Long medicationId, CustomUserDetails principal);

    MedicationReminderResponse create(
            Long medicationId, CustomUserDetails principal, CreateMedicationReminderRequest request);

    MedicationReminderResponse markTaken(Long reminderId, CustomUserDetails principal);
}
