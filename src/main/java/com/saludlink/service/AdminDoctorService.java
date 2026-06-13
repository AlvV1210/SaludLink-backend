package com.saludlink.service;

import com.saludlink.model.dto.AdminDoctorCreateDTO;
import com.saludlink.model.dto.DoctorResponseDTO;

public interface AdminDoctorService {

    DoctorResponseDTO registerDoctor(AdminDoctorCreateDTO dto);
}
