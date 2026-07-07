package com.saludlink.medication.service;

import com.saludlink.auth.security.CustomUserDetails;
import com.saludlink.medication.dto.CreateMedicationIntakeRequest;
import com.saludlink.medication.dto.MedicationIntakeResponse;
import java.util.List;

public interface IMedicationIntakeService {

    List<MedicationIntakeResponse> listForMedication(Long medicationId, CustomUserDetails principal);

    MedicationIntakeResponse record(
            Long medicationId, CustomUserDetails principal, CreateMedicationIntakeRequest request);
}
