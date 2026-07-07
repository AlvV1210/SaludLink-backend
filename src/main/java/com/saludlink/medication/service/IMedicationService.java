package com.saludlink.medication.service;

import com.saludlink.medication.dto.CreateMedicationRequest;
import com.saludlink.medication.dto.MedicationResponse;
import java.util.List;

public interface IMedicationService {

    MedicationResponse addMedication(Long patientId, CreateMedicationRequest request);

    List<MedicationResponse> listByPatient(Long patientId);

    void deactivate(Long medicationId);
}
