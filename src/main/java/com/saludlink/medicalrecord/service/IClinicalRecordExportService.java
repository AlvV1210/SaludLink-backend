package com.saludlink.medicalrecord.service;

import com.saludlink.medicalrecord.dto.ExportClinicalRecordRequest;
import com.saludlink.medicalrecord.dto.ExportClinicalRecordResponse;

public interface IClinicalRecordExportService {

    ExportClinicalRecordResponse createExport(Long userId, ExportClinicalRecordRequest request);

    byte[] downloadByAccessCode(String accessCode);
}
