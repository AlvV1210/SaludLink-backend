package com.saludlink.application.service;

import com.saludlink.application.dto.AppointmentRequestDTO;
import com.saludlink.application.dto.AppointmentResponseDTO;
import com.saludlink.domain.model.enums.AppointmentStatus;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(Long patientId, AppointmentRequestDTO dto);

    List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId);

    List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId);

    void cancelAppointment(Long appointmentId);

    void updateAppointmentStatus(Long id, AppointmentStatus status);
}
