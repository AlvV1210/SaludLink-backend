package com.saludlink.service.impl;

import com.saludlink.model.dto.AppointmentRequestDTO;
import com.saludlink.model.dto.AppointmentResponseDTO;
import com.saludlink.model.entity.Appointment;
import com.saludlink.model.enums.AppointmentStatus;
import com.saludlink.repository.AppointmentRepository;
import com.saludlink.repository.DoctorRepository;
import com.saludlink.repository.PatientRepository;
import com.saludlink.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public AppointmentResponseDTO createAppointment(Long patientId, AppointmentRequestDTO dto) {
        var patient =
                patientRepository
                        .findById(patientId)
                        .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + patientId));
        var doctor =
                doctorRepository
                        .findById(dto.getDoctorId())
                        .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado: " + dto.getDoctorId()));

        Appointment appointment =
                Appointment.builder()
                        .patient(patient)
                        .doctor(doctor)
                        .appointmentDate(dto.getAppointmentDate())
                        .status(AppointmentStatus.PENDING)
                        .modality(dto.getModality())
                        .notes(dto.getNotes())
                        .build();
        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream().map(this::toResponse).toList();
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada: " + appointmentId));
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    @Override
    public void cancelAppointmentForPatient(Long appointmentId, Long patientId) {
        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada: " + appointmentId));
        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("La cita no pertenece al paciente autenticado");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    @Override
    public void updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada: " + id));
        appointment.setStatus(status);
    }

    private AppointmentResponseDTO toResponse(Appointment a) {
        var doctor = a.getDoctor();
        var user = doctor.getUser();
        String doctorName = user.getFirstName() + " " + user.getLastName();
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .doctorName(doctorName)
                .specialty(doctor.getSpecialty())
                .appointmentDate(a.getAppointmentDate())
                .modality(a.getModality())
                .status(a.getStatus())
                .notes(a.getNotes())
                .build();
    }
}
