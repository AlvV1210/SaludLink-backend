package com.saludlink.application.dto;

import com.saludlink.domain.model.entity.MedicalDocument;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDocumentResponseDTO {

    private Long id;
    private Long patientId;
    private String fileName;
    private String fileUrl;
    private String documentType;
    private LocalDateTime uploadedAt;

    public static MedicalDocumentResponseDTO fromEntity(MedicalDocument d) {
        return MedicalDocumentResponseDTO.builder()
                .id(d.getId())
                .patientId(d.getPatient().getId())
                .fileName(d.getFileName())
                .fileUrl(d.getFileUrl())
                .documentType(d.getDocumentType())
                .uploadedAt(d.getUploadedAt())
                .build();
    }
}
