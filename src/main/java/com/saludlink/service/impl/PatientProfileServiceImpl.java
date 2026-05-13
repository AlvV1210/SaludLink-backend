package com.saludlink.service.impl;

import com.saludlink.model.dto.PatientProfileResponseDTO;
import com.saludlink.model.dto.PatientProfileUpdateDTO;
import com.saludlink.model.entity.Patient;
import com.saludlink.model.entity.User;
import com.saludlink.repository.PatientRepository;
import com.saludlink.repository.UserRepository;
import com.saludlink.service.PatientProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientProfileResponseDTO getProfileForUser(Long userId) {
        Patient patient =
                patientRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException("Perfil de paciente no encontrado"));
        return toResponse(patient);
    }

    @Override
    public PatientProfileResponseDTO updateProfile(Long userId, PatientProfileUpdateDTO dto) {
        Patient patient =
                patientRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException("Perfil de paciente no encontrado"));
        User user = patient.getUser();

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getBirthDate() != null) {
            patient.setBirthDate(dto.getBirthDate());
        }
        if (dto.getBloodType() != null) {
            patient.setBloodType(dto.getBloodType());
        }
        if (dto.getAllergies() != null) {
            patient.setAllergies(dto.getAllergies());
        }
        if (dto.getMedicalHistory() != null) {
            patient.setMedicalHistory(dto.getMedicalHistory());
        }
        if (dto.getInsuranceInfo() != null) {
            patient.setInsuranceInfo(dto.getInsuranceInfo());
        }

        userRepository.save(user);
        patientRepository.save(patient);
        return toResponse(patient);
    }

    private PatientProfileResponseDTO toResponse(Patient p) {
        User u = p.getUser();
        return PatientProfileResponseDTO.builder()
                .userId(u.getId())
                .patientId(p.getId())
                .email(u.getEmail())
                .role(u.getRole())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .phone(u.getPhone())
                .birthDate(p.getBirthDate())
                .bloodType(p.getBloodType())
                .allergies(p.getAllergies())
                .medicalHistory(p.getMedicalHistory())
                .insuranceInfo(p.getInsuranceInfo())
                .build();
    }
}
