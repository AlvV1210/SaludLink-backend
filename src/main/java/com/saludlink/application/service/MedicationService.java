package com.saludlink.application.service;

import com.saludlink.application.dto.MedicationRequestDTO;
import com.saludlink.domain.model.entity.Medication;
import java.util.List;

public interface MedicationService {

    Medication addMedication(Long patientId, MedicationRequestDTO dto);

    List<Medication> getMedicationsByPatient(Long patientId);

    void deactivateMedication(Long medicationId);
}
