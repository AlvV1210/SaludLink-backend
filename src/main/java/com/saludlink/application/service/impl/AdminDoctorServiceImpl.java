package com.saludlink.application.service.impl;

import com.saludlink.application.dto.AdminDoctorCreateDTO;
import com.saludlink.application.dto.DoctorResponseDTO;
import com.saludlink.domain.model.entity.Doctor;
import com.saludlink.domain.model.entity.User;
import com.saludlink.domain.model.enums.UserRole;
import com.saludlink.infrastructure.persistence.repository.DoctorRepository;
import com.saludlink.infrastructure.persistence.repository.UserRepository;
import com.saludlink.application.service.AdminDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDoctorServiceImpl implements AdminDoctorService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DoctorResponseDTO registerDoctor(AdminDoctorCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        if (doctorRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new IllegalArgumentException("La matrícula ya está registrada");
        }
        User user =
                User.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .phone(dto.getPhone())
                        .role(UserRole.DOCTOR)
                        .build();
        User savedUser = userRepository.save(user);
        Doctor doctor =
                Doctor.builder()
                        .user(savedUser)
                        .specialty(dto.getSpecialty())
                        .licenseNumber(dto.getLicenseNumber())
                        .verified(false)
                        .biography(dto.getBiography())
                        .consultationFee(dto.getConsultationFee())
                        .build();
        Doctor saved = doctorRepository.save(doctor);
        return DoctorResponseDTO.fromEntity(saved);
    }
}
