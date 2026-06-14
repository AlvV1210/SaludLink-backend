package com.saludlink.application.service;

import com.saludlink.application.dto.MedicationIntakeRequestDTO;
import com.saludlink.application.dto.MedicationIntakeResponseDTO;
import com.saludlink.infrastructure.security.CustomUserDetails;
import java.util.List;

public interface MedicationIntakeService {

    List<MedicationIntakeResponseDTO> listForMedication(Long medicationId, CustomUserDetails principal);

    MedicationIntakeResponseDTO record(
            Long medicationId, CustomUserDetails principal, MedicationIntakeRequestDTO dto);
}
