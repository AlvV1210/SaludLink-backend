package com.saludlink.service;

import com.saludlink.model.dto.PatientProfileResponseDTO;
import com.saludlink.model.dto.PatientProfileUpdateDTO;

public interface PatientProfileService {

    PatientProfileResponseDTO getProfileForUser(Long userId);

    PatientProfileResponseDTO updateProfile(Long userId, PatientProfileUpdateDTO dto);
}
