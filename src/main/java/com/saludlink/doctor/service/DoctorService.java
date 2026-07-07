package com.saludlink.doctor.service;

import com.saludlink.auth.exception.EmailAlreadyExistsException;
import com.saludlink.auth.model.User;
import com.saludlink.auth.model.UserRole;
import com.saludlink.auth.repository.UserRepository;
import com.saludlink.doctor.dto.CreateDoctorRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityResponse;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.doctor.dto.SubmitCredentialsRequest;
import com.saludlink.doctor.exception.DoctorNotFoundException;
import com.saludlink.doctor.exception.LicenseAlreadyExistsException;
import com.saludlink.doctor.mapper.DoctorMapper;
import com.saludlink.doctor.model.Doctor;
import com.saludlink.doctor.model.DoctorAvailability;
import com.saludlink.doctor.repository.DoctorAvailabilityRepository;
import com.saludlink.doctor.repository.DoctorRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> listVerified() {
        return doctorRepository.findByVerifiedTrue().stream().map(doctorMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> listBySpecialty(String specialty) {
        String needle = normalizeSearchKey(specialty);
        if (needle.isEmpty()) {
            return listVerified();
        }
        return doctorRepository.findByVerifiedTrue().stream()
                .filter(doctor -> normalizeSearchKey(doctor.getSpecialty()).contains(needle))
                .map(doctorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getById(Long id) {
        return doctorMapper.toResponse(requireDoctor(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listSpecialties() {
        return doctorRepository.findDistinctSpecialties();
    }

    @Override
    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        return doctorMapper.toResponse(createDoctorEntity(request));
    }

    @Override
    @Transactional
    public Doctor createDoctorEntity(CreateDoctorRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }
        if (doctorRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new LicenseAlreadyExistsException();
        }
        User user =
                User.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .phone(request.phone())
                        .role(UserRole.DOCTOR)
                        .build();
        User savedUser = userRepository.save(user);
        Doctor doctor =
                Doctor.builder()
                        .user(savedUser)
                        .specialty(request.specialty())
                        .licenseNumber(request.licenseNumber())
                        .verified(false)
                        .biography(request.biography())
                        .consultationFee(request.consultationFee())
                        .build();
        return doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse submitCredentials(Long userId, SubmitCredentialsRequest request) {
        Doctor doctor = resolveDoctorForUser(userId, request);
        doctor.setLicenseDocumentUrl(request.licenseDocumentUrl());
        doctor.setVerified(true);
        return doctorMapper.toResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailabilityResponse> listAvailability(Long userId) {
        Doctor doctor = requireDoctorByUser(userId);
        return availabilityRepository.findByDoctorId(doctor.getId()).stream()
                .map(doctorMapper::toAvailabilityResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorAvailabilityResponse upsertAvailability(Long userId, DoctorAvailabilityRequest request) {
        Doctor doctor = requireDoctorByUser(userId);
        DoctorAvailability availability =
                DoctorAvailability.builder()
                        .doctor(doctor)
                        .dayOfWeek(request.dayOfWeek())
                        .startTime(request.startTime())
                        .endTime(request.endTime())
                        .blocked(request.blocked())
                        .build();
        return doctorMapper.toAvailabilityResponse(availabilityRepository.save(availability));
    }

    @Override
    @Transactional(readOnly = true)
    public Long requireDoctorIdForUser(Long userId) {
        return requireDoctorByUser(userId).getId();
    }

    private Doctor requireDoctor(Long id) {
        return doctorRepository.findDetailById(id).orElseThrow(DoctorNotFoundException::new);
    }

    private Doctor requireDoctorByUser(Long userId) {
        return doctorRepository.findByUserId(userId).orElseThrow(DoctorNotFoundException::new);
    }

    private Doctor resolveDoctorForUser(Long userId, SubmitCredentialsRequest request) {
        return doctorRepository
                .findByUserId(userId)
                .orElseGet(() -> createDoctorProfileForExistingUser(userId, request));
    }

    private Doctor createDoctorProfileForExistingUser(Long userId, SubmitCredentialsRequest request) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(DoctorNotFoundException::new);
        if (user.getRole() != UserRole.DOCTOR) {
            throw new DoctorNotFoundException();
        }

        String specialty = normalizeRequiredField(request.specialty(), "La especialidad es obligatoria.");
        String licenseNumber =
                normalizeRequiredField(request.licenseNumber(), "La matricula es obligatoria.");
        if (doctorRepository.existsByLicenseNumber(licenseNumber)) {
            throw new LicenseAlreadyExistsException();
        }

        BigDecimal consultationFee = request.consultationFee();
        if (consultationFee != null && consultationFee.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La tarifa de consulta debe ser mayor a cero.");
        }

        return doctorRepository.save(
                Doctor.builder()
                        .user(user)
                        .specialty(specialty)
                        .licenseNumber(licenseNumber)
                        .consultationFee(consultationFee)
                        .verified(true)
                        .build());
    }

    private String normalizeSearchKey(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
    }

    private String normalizeRequiredField(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(message);
        }
        return value.trim();
    }
}
