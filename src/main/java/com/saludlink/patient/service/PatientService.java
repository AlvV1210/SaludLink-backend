package com.saludlink.patient.service;

import com.saludlink.auth.repository.UserRepository;
import com.saludlink.patient.dto.NotificationPreferencesRequest;
import com.saludlink.patient.dto.PatientProfileResponse;
import com.saludlink.patient.dto.UpdatePatientProfileRequest;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.mapper.PatientMapper;
import com.saludlink.patient.model.Patient;
import com.saludlink.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional(readOnly = true)
    public PatientProfileResponse getProfile(Long userId) {
        return patientMapper.toResponse(requirePatient(userId));
    }

    @Override
    @Transactional
    public PatientProfileResponse updateProfile(Long userId, UpdatePatientProfileRequest request) {
        Patient patient = requirePatient(userId);
        var user = patient.getUser();
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.phone() != null) user.setPhone(request.phone());
        if (request.birthDate() != null) patient.setBirthDate(request.birthDate());
        if (request.bloodType() != null) patient.setBloodType(request.bloodType());
        if (request.allergies() != null) patient.setAllergies(request.allergies());
        if (request.medicalHistory() != null) patient.setMedicalHistory(request.medicalHistory());
        if (request.insuranceInfo() != null) patient.setInsuranceInfo(request.insuranceInfo());
        userRepository.save(user);
        patientRepository.save(patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public PatientProfileResponse updateNotificationPreferences(
            Long userId, NotificationPreferencesRequest request) {
        Patient patient = requirePatient(userId);
        if (request.alertSound() != null) patient.setAlertSound(request.alertSound());
        if (request.alertFrequency() != null) patient.setAlertFrequency(request.alertFrequency());
        patientRepository.save(patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Long requirePatientIdForUser(Long userId) {
        return requirePatient(userId).getId();
    }

    private Patient requirePatient(Long userId) {
        return patientRepository
                .findByUserId(userId)
                .orElseThrow(PatientProfileNotFoundException::new);
    }
}
