package com.saludlink.ai.tool;

import com.saludlink.ai.security.AiUserContext;
import com.saludlink.appointment.dto.AppointmentResponse;
import com.saludlink.appointment.dto.CreateAppointmentRequest;
import com.saludlink.appointment.model.AppointmentModality;
import com.saludlink.appointment.service.IAppointmentService;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.doctor.service.IDoctorService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentTool {

    private final AiUserContext userContext;
    private final IDoctorService doctorService;
    private final IAppointmentService appointmentService;

    @Tool(description = "Lista médicos verificados filtrados por especialidad (ej. pediatría, cardiología).")
    public List<DoctorResponse> findDoctorsBySpecialty(
            @ToolParam(description = "nombre de la especialidad en español") String specialty) {
        return doctorService.listBySpecialty(specialty);
    }

    @Tool(description = "Lista las citas del paciente autenticado.")
    public List<AppointmentResponse> listMyAppointments() {
        Long patientId = userContext.currentPatientId();
        return appointmentService.listByPatient(patientId);
    }

    @Tool(description = "Reserva una cita para el paciente autenticado. modality: IN_PERSON o TELEMEDICINE.")
    public AppointmentResponse bookAppointment(
            @ToolParam(description = "id del médico") Long doctorId,
            @ToolParam(description = "fecha y hora ISO, ej. 2026-07-14T09:00:00") LocalDateTime appointmentDate,
            @ToolParam(description = "IN_PERSON o TELEMEDICINE") String modality,
            @ToolParam(description = "notas opcionales") String notes) {
        Long patientId = userContext.currentPatientId();
        AppointmentModality parsed =
                "TELEMEDICINE".equalsIgnoreCase(modality) || "virtual".equalsIgnoreCase(modality)
                        ? AppointmentModality.TELEMEDICINE
                        : AppointmentModality.IN_PERSON;
        return appointmentService.create(
                patientId, new CreateAppointmentRequest(doctorId, appointmentDate, parsed, notes));
    }
}
