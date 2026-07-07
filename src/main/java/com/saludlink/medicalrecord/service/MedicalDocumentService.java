package com.saludlink.medicalrecord.service;

import com.saludlink.medicalrecord.dto.CreateMedicalDocumentRequest;
import com.saludlink.medicalrecord.dto.MedicalDocumentResponse;
import com.saludlink.medicalrecord.exception.MedicalDocumentNotFoundException;
import com.saludlink.medicalrecord.mapper.MedicalDocumentMapper;
import com.saludlink.medicalrecord.model.MedicalDocument;
import com.saludlink.medicalrecord.repository.MedicalDocumentRepository;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.model.Patient;
import com.saludlink.patient.repository.PatientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicalDocumentService implements IMedicalDocumentService {

    private final MedicalDocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    private final MedicalDocumentMapper documentMapper;

    private Patient requirePatient(Long userId) {
        return patientRepository.findByUserId(userId).orElseThrow(PatientProfileNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalDocumentResponse> listForUser(Long userId) {
        Patient patient = requirePatient(userId);
        return documentRepository.findByPatientId(patient.getId()).stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MedicalDocumentResponse create(Long userId, CreateMedicalDocumentRequest request) {
        Patient patient = requirePatient(userId);
        MedicalDocument doc =
                MedicalDocument.builder()
                        .patient(patient)
                        .fileName(request.fileName())
                        .fileUrl(request.fileUrl())
                        .documentType(request.documentType())
                        .build();
        return documentMapper.toResponse(documentRepository.save(doc));
    }

    @Override
    @Transactional
    public void deleteForUser(Long userId, Long documentId) {
        Patient patient = requirePatient(userId);
        MedicalDocument doc =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(MedicalDocumentNotFoundException::new);
        if (!doc.getPatient().getId().equals(patient.getId())) {
            throw new AccessDeniedException("El documento no pertenece al paciente autenticado");
        }
        documentRepository.delete(doc);
    }
}
