package com.saludlink.medicalrecord.service;

import com.saludlink.medicalrecord.dto.CreateMedicalDocumentRequest;
import com.saludlink.medicalrecord.dto.MedicalDocumentResponse;
import java.util.List;

public interface IMedicalDocumentService {

    List<MedicalDocumentResponse> listForUser(Long userId);

    MedicalDocumentResponse create(Long userId, CreateMedicalDocumentRequest request);

    void deleteForUser(Long userId, Long documentId);
}
