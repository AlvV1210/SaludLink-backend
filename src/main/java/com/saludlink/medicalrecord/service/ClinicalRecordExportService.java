package com.saludlink.medicalrecord.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.saludlink.appointment.model.Appointment;
import com.saludlink.appointment.repository.AppointmentRepository;
import com.saludlink.medicalrecord.dto.ExportClinicalRecordRequest;
import com.saludlink.medicalrecord.dto.ExportClinicalRecordResponse;
import com.saludlink.medicalrecord.model.ClinicalRecordExport;
import com.saludlink.medicalrecord.model.MedicalDocument;
import com.saludlink.medicalrecord.repository.ClinicalRecordExportRepository;
import com.saludlink.medicalrecord.repository.MedicalDocumentRepository;
import com.saludlink.medication.model.Medication;
import com.saludlink.medication.repository.MedicationRepository;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.model.Patient;
import com.saludlink.patient.repository.PatientRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import com.saludlink.shared.exception.ResourceNotFoundException;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClinicalRecordExportService implements IClinicalRecordExportService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;
    private final MedicalDocumentRepository documentRepository;
    private final ClinicalRecordExportRepository exportRepository;

    @Override
    @Transactional
    public ExportClinicalRecordResponse createExport(Long userId, ExportClinicalRecordRequest request) {
        if (request.toDate().isBefore(request.fromDate())) {
            throw new BusinessRuleException("El rango de fechas no es valido");
        }
        Patient patient = requirePatient(userId);
        String accessCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String fileName = "historial-" + patient.getId() + "-" + request.fromDate() + ".pdf";
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
        ClinicalRecordExport export =
                ClinicalRecordExport.builder()
                        .patient(patient)
                        .fromDate(request.fromDate())
                        .toDate(request.toDate())
                        .accessCode(accessCode)
                        .fileName(fileName)
                        .expiresAt(expiresAt)
                        .build();
        ClinicalRecordExport saved = exportRepository.save(export);
        return new ExportClinicalRecordResponse(saved.getId(), fileName, accessCode, expiresAt);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadByAccessCode(String accessCode) {
        ClinicalRecordExport export =
                exportRepository
                        .findByAccessCodeAndExpiresAtAfter(accessCode, LocalDateTime.now())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Exportacion no encontrada o expirada"));
        return buildPdf(export);
    }

    private byte[] buildPdf(ClinicalRecordExport export) {
        Patient patient = export.getPatient();
        LocalDateTime from = export.getFromDate().atStartOfDay();
        LocalDateTime to = export.getToDate().atTime(LocalTime.MAX);
        List<Appointment> appointments =
                appointmentRepository.findPatientHistory(patient.getId(), null, from, to);
        List<Medication> medications = medicationRepository.findByPatientId(patient.getId());
        List<MedicalDocument> documents = documentRepository.findByPatientId(patient.getId());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font body = FontFactory.getFont(FontFactory.HELVETICA, 11);
            document.add(new Paragraph("SaludLink - Historial Clinico", title));
            document.add(new Paragraph(" "));
            document.add(
                    new Paragraph(
                            "Paciente: "
                                    + patient.getUser().getFirstName()
                                    + " "
                                    + patient.getUser().getLastName(),
                            body));
            document.add(
                    new Paragraph(
                            "Periodo: " + export.getFromDate() + " a " + export.getToDate(), body));
            document.add(new Paragraph("Codigo de acceso: " + export.getAccessCode(), body));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Citas medicas", title));
            for (Appointment a : appointments) {
                document.add(
                        new Paragraph(
                                "- "
                                        + a.getAppointmentDate()
                                        + " | "
                                        + a.getDoctor().getSpecialty()
                                        + " | "
                                        + a.getStatus(),
                                body));
            }
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Medicamentos activos", title));
            for (Medication m : medications) {
                if (m.isActive()) {
                    document.add(
                            new Paragraph(
                                    "- " + m.getName() + " " + m.getDosage() + " " + m.getFrequency(),
                                    body));
                }
            }
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Documentos", title));
            for (MedicalDocument d : documents) {
                document.add(
                        new Paragraph(
                                "- " + d.getUploadedAt() + " | " + d.getFileName() + " | " + d.getFileUrl(),
                                body));
            }
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new BusinessRuleException("No se pudo generar el PDF del historial");
        }
    }

    private Patient requirePatient(Long userId) {
        return patientRepository.findByUserId(userId).orElseThrow(PatientProfileNotFoundException::new);
    }
}
