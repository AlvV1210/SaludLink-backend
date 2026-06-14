package com.saludlink.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDocumentRequestDTO {

    private String fileName;

    @NotBlank private String fileUrl;

    private String documentType;
}
