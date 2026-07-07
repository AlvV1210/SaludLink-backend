package com.saludlink.doctor.service;

import com.saludlink.doctor.dto.CreateDoctorRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityResponse;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.doctor.dto.SubmitCredentialsRequest;
import com.saludlink.doctor.model.Doctor;
import java.util.List;

public interface IDoctorService {

    List<DoctorResponse> listVerified();

    List<DoctorResponse> listBySpecialty(String specialty);

    DoctorResponse getById(Long id);

    List<String> listSpecialties();

    DoctorResponse createDoctor(CreateDoctorRequest request);

    Doctor createDoctorEntity(CreateDoctorRequest request);

    DoctorResponse submitCredentials(Long userId, SubmitCredentialsRequest request);

    List<DoctorAvailabilityResponse> listAvailability(Long userId);

    DoctorAvailabilityResponse upsertAvailability(Long userId, DoctorAvailabilityRequest request);

    Long requireDoctorIdForUser(Long userId);
}
