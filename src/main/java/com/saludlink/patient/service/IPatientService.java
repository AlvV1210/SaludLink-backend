package com.saludlink.patient.service;

import com.saludlink.patient.dto.NotificationPreferencesRequest;
import com.saludlink.patient.dto.PatientProfileResponse;
import com.saludlink.patient.dto.UpdatePatientProfileRequest;

public interface IPatientService {

    PatientProfileResponse getProfile(Long userId);

    PatientProfileResponse updateProfile(Long userId, UpdatePatientProfileRequest request);

    PatientProfileResponse updateNotificationPreferences(
            Long userId, NotificationPreferencesRequest request);

    Long requirePatientIdForUser(Long userId);
}
