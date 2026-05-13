package com.saludlink.service;

import com.saludlink.model.dto.MedicalDocumentRequestDTO;
import com.saludlink.model.dto.MedicalDocumentResponseDTO;
import java.util.List;

public interface MedicalDocumentService {

    List<MedicalDocumentResponseDTO> listForUser(Long userId);

    MedicalDocumentResponseDTO create(Long userId, MedicalDocumentRequestDTO dto);

    void deleteForUser(Long userId, Long documentId);
}
