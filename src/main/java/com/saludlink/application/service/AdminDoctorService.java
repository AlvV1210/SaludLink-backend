package com.saludlink.application.service;

import com.saludlink.application.dto.AdminDoctorCreateDTO;
import com.saludlink.application.dto.DoctorResponseDTO;

public interface AdminDoctorService {

    DoctorResponseDTO registerDoctor(AdminDoctorCreateDTO dto);
}
