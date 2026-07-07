package com.saludlink.appointment.service;

import com.saludlink.appointment.dto.AppointmentResponse;
import com.saludlink.appointment.dto.CreateAppointmentRequest;
import com.saludlink.appointment.dto.RescheduleAppointmentRequest;
import com.saludlink.appointment.dto.UpdateAppointmentStatusRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {

    AppointmentResponse create(Long patientId, CreateAppointmentRequest request);

    List<AppointmentResponse> listByPatient(Long patientId);

    List<AppointmentResponse> listByDoctor(Long doctorId);

    List<AppointmentResponse> patientHistory(
            Long patientId, String specialty, LocalDateTime from, LocalDateTime to);

    void cancel(Long appointmentId);

    AppointmentResponse reschedule(Long appointmentId, Long patientUserId, RescheduleAppointmentRequest request);

    void updateStatus(Long appointmentId, UpdateAppointmentStatusRequest request);
}
