package com.saludlink.patient.service;

import com.saludlink.patient.dto.CreateDependentRequest;
import com.saludlink.patient.dto.DependentResponse;
import com.saludlink.patient.exception.DependentNotFoundException;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.mapper.DependentMapper;
import com.saludlink.patient.model.DependentProfile;
import com.saludlink.patient.model.Patient;
import com.saludlink.patient.repository.DependentProfileRepository;
import com.saludlink.patient.repository.PatientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DependentService implements IDependentService {

    private final PatientRepository patientRepository;
    private final DependentProfileRepository dependentRepository;
    private final DependentMapper dependentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DependentResponse> listForGuardian(Long userId) {
        Patient guardian = requireGuardian(userId);
        return dependentRepository.findByGuardianIdAndActiveTrue(guardian.getId()).stream()
                .map(dependentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DependentResponse create(Long userId, CreateDependentRequest request) {
        Patient guardian = requireGuardian(userId);
        DependentProfile dependent =
                DependentProfile.builder()
                        .guardian(guardian)
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .birthDate(request.birthDate())
                        .relationship(request.relationship())
                        .allergies(request.allergies())
                        .medicalHistory(request.medicalHistory())
                        .active(true)
                        .build();
        return dependentMapper.toResponse(dependentRepository.save(dependent));
    }

    @Override
    @Transactional(readOnly = true)
    public DependentResponse getById(Long userId, Long dependentId) {
        Patient guardian = requireGuardian(userId);
        return dependentMapper.toResponse(requireOwnedDependent(dependentId, guardian.getId()));
    }

    @Override
    @Transactional
    public void deactivate(Long userId, Long dependentId) {
        Patient guardian = requireGuardian(userId);
        DependentProfile dependent = requireOwnedDependent(dependentId, guardian.getId());
        dependent.setActive(false);
    }

    private Patient requireGuardian(Long userId) {
        return patientRepository.findByUserId(userId).orElseThrow(PatientProfileNotFoundException::new);
    }

    private DependentProfile requireOwnedDependent(Long dependentId, Long guardianPatientId) {
        return dependentRepository
                .findByIdAndGuardianId(dependentId, guardianPatientId)
                .orElseThrow(DependentNotFoundException::new);
    }
}
