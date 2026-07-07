package com.saludlink.medicalrecord.mapper;

import com.saludlink.medicalrecord.dto.MedicalDocumentResponse;
import com.saludlink.medicalrecord.model.MedicalDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalDocumentMapper {

    MedicalDocumentResponse toResponse(MedicalDocument document);
}
