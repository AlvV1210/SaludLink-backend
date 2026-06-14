package com.saludlink.application.service;

import com.saludlink.application.dto.MedicalDocumentRequestDTO;
import com.saludlink.application.dto.MedicalDocumentResponseDTO;
import java.util.List;

public interface MedicalDocumentService {

    List<MedicalDocumentResponseDTO> listForUser(Long userId);

    MedicalDocumentResponseDTO create(Long userId, MedicalDocumentRequestDTO dto);

    void deleteForUser(Long userId, Long documentId);
}
