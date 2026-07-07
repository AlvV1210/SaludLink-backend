package com.saludlink.appointment.service;

import com.saludlink.appointment.dto.AppointmentResponse;
import com.saludlink.appointment.dto.CreateAppointmentRequest;
import com.saludlink.appointment.dto.RescheduleAppointmentRequest;
import com.saludlink.appointment.dto.UpdateAppointmentStatusRequest;
import com.saludlink.appointment.exception.AppointmentConflictException;
import com.saludlink.appointment.exception.AppointmentNotFoundException;
import com.saludlink.appointment.mapper.AppointmentMapper;
import com.saludlink.appointment.model.Appointment;
import com.saludlink.appointment.model.AppointmentStatus;
import com.saludlink.appointment.repository.AppointmentRepository;
import com.saludlink.doctor.exception.DoctorNotFoundException;
import com.saludlink.doctor.repository.DoctorRepository;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.repository.PatientRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public AppointmentResponse create(Long patientId, CreateAppointmentRequest request) {
        var patient =
                patientRepository.findById(patientId).orElseThrow(PatientProfileNotFoundException::new);
        var doctor =
                doctorRepository
                        .findById(request.doctorId())
                        .orElseThrow(DoctorNotFoundException::new);
        validateNoOverlap(doctor.getId(), request.appointmentDate(), null);
        Appointment appointment =
                Appointment.builder()
                        .patient(patient)
                        .doctor(doctor)
                        .appointmentDate(request.appointmentDate())
                        .status(AppointmentStatus.SCHEDULED)
                        .modality(request.modality())
                        .notes(request.notes())
                        .build();
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> listByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> listByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> patientHistory(
            Long patientId, String specialty, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.findPatientHistory(patientId, specialty, from, to).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void cancel(Long appointmentId) {
        Appointment appointment = requireAppointment(appointmentId);
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new BusinessRuleException(
                    "La cancelacion debe realizarse con al menos 24 horas de anticipacion");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    @Override
    @Transactional
    public AppointmentResponse reschedule(
            Long appointmentId, Long patientUserId, RescheduleAppointmentRequest request) {
        Appointment appointment = requireAppointment(appointmentId);
        if (!appointment.getPatient().getUser().getId().equals(patientUserId)) {
            throw new BusinessRuleException("No puedes reprogramar esta cita");
        }
        validateNoOverlap(
                appointment.getDoctor().getId(), request.appointmentDate(), appointment.getId());
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    @Transactional
    public void updateStatus(Long appointmentId, UpdateAppointmentStatusRequest request) {
        Appointment appointment = requireAppointment(appointmentId);
        appointment.setStatus(request.status());
        if (request.notes() != null) {
            appointment.setNotes(request.notes());
        }
    }

    private void validateNoOverlap(Long doctorId, LocalDateTime date, Long excludeId) {
        if (appointmentRepository.existsOverlapping(doctorId, date, excludeId)) {
            throw new AppointmentConflictException();
        }
    }

    private Appointment requireAppointment(Long id) {
        return appointmentRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);
    }
}
