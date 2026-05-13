package com.saludlink.service;

import com.saludlink.model.dto.MedicationRequestDTO;
import com.saludlink.model.entity.Medication;
import java.util.List;

public interface MedicationService {

    Medication addMedication(Long patientId, MedicationRequestDTO dto);

    List<Medication> getMedicationsByPatient(Long patientId);

    void deactivateMedication(Long medicationId);

    void deactivateMedicationForPatient(Long medicationId, Long patientId);
}
