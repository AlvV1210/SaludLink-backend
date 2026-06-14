package com.saludlink.application.service;

import com.saludlink.application.dto.PatientProfileResponseDTO;
import com.saludlink.application.dto.PatientProfileUpdateDTO;

public interface PatientProfileService {

    PatientProfileResponseDTO getProfileForUser(Long userId);

    PatientProfileResponseDTO updateProfile(Long userId, PatientProfileUpdateDTO dto);
}
