package com.saludlink.application.service.impl;

import com.saludlink.application.dto.MedicalDocumentRequestDTO;
import com.saludlink.application.dto.MedicalDocumentResponseDTO;
import com.saludlink.domain.model.entity.MedicalDocument;
import com.saludlink.domain.model.entity.Patient;
import com.saludlink.infrastructure.persistence.repository.MedicalDocumentRepository;
import com.saludlink.infrastructure.persistence.repository.PatientRepository;
import com.saludlink.application.service.MedicalDocumentService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    private final MedicalDocumentRepository medicalDocumentRepository;
    private final PatientRepository patientRepository;

    private Patient requirePatientForUser(Long userId) {
        return patientRepository
                .findByUserId(userId)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Perfil de paciente no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalDocumentResponseDTO> listForUser(Long userId) {
        Patient patient = requirePatientForUser(userId);
        return medicalDocumentRepository.findByPatientId(patient.getId()).stream()
                .map(MedicalDocumentResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public MedicalDocumentResponseDTO create(Long userId, MedicalDocumentRequestDTO dto) {
        Patient patient = requirePatientForUser(userId);
        MedicalDocument doc =
                MedicalDocument.builder()
                        .patient(patient)
                        .fileName(dto.getFileName())
                        .fileUrl(dto.getFileUrl())
                        .documentType(dto.getDocumentType())
                        .build();
        return MedicalDocumentResponseDTO.fromEntity(medicalDocumentRepository.save(doc));
    }

    @Override
    public void deleteForUser(Long userId, Long documentId) {
        Patient patient = requirePatientForUser(userId);
        MedicalDocument doc =
                medicalDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado: " + documentId));
        if (!doc.getPatient().getId().equals(patient.getId())) {
            throw new AccessDeniedException("El documento no pertenece al paciente autenticado");
        }
        medicalDocumentRepository.delete(doc);
    }
}
