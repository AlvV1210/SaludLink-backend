package com.saludlink.service;

import com.saludlink.model.dto.MedicationRequestDTO;
import com.saludlink.model.dto.MedicationResponseDTO;
import java.util.List;

public interface MedicationService {

    MedicationResponseDTO addMedication(Long patientId, MedicationRequestDTO dto);

    List<MedicationResponseDTO> getMedicationsByPatient(Long patientId);

    void deactivateMedication(Long medicationId);

    void deactivateMedicationForPatient(Long medicationId, Long patientId);
}
