package com.saludlink.service;

import com.saludlink.model.dto.MedicationIntakeRequestDTO;
import com.saludlink.model.dto.MedicationIntakeResponseDTO;
import com.saludlink.security.CustomUserDetails;
import java.util.List;

public interface MedicationIntakeService {

    List<MedicationIntakeResponseDTO> listForMedication(Long medicationId, CustomUserDetails principal);

    MedicationIntakeResponseDTO record(
            Long medicationId, CustomUserDetails principal, MedicationIntakeRequestDTO dto);
}
