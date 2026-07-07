package com.saludlink.ai.tool;

import com.saludlink.ai.security.AiUserContext;
import com.saludlink.appointment.dto.AppointmentResponse;
import com.saludlink.appointment.service.IAppointmentService;
import com.saludlink.doctor.dto.DoctorAvailabilityRequest;
import com.saludlink.doctor.dto.DoctorAvailabilityResponse;
import com.saludlink.doctor.service.IDoctorService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleTool {

    private final AiUserContext userContext;
    private final IAppointmentService appointmentService;
    private final IDoctorService doctorService;

    @Tool(description = "Lista todas las citas del médico autenticado.")
    public List<AppointmentResponse> listMyAppointments() {
        Long doctorId = userContext.currentDoctorId();
        return appointmentService.listByDoctor(doctorId);
    }

    @Tool(description = "Lista las citas del médico autenticado para una fecha (yyyy-MM-dd).")
    public List<AppointmentResponse> listAppointmentsOnDate(
            @ToolParam(description = "fecha en formato yyyy-MM-dd") LocalDate date) {
        return listMyAppointments().stream()
                .filter(a -> a.appointmentDate().toLocalDate().equals(date))
                .toList();
    }

    @Tool(description = "Cuenta las citas del médico autenticado para una fecha (yyyy-MM-dd).")
    public int countAppointmentsOnDate(
            @ToolParam(description = "fecha en formato yyyy-MM-dd") LocalDate date) {
        return listAppointmentsOnDate(date).size();
    }

    @Tool(description = "Lista la disponibilidad semanal del médico autenticado.")
    public List<DoctorAvailabilityResponse> listMyAvailability() {
        Long userId = userContext.currentUserId();
        return doctorService.listAvailability(userId);
    }

    @Tool(description = "Bloquea o actualiza un bloque horario del médico autenticado.")
    public DoctorAvailabilityResponse blockTimeSlot(
            @ToolParam(description = "día de la semana, ej. MONDAY") DayOfWeek dayOfWeek,
            @ToolParam(description = "hora inicio HH:mm") LocalTime startTime,
            @ToolParam(description = "hora fin HH:mm") LocalTime endTime) {
        Long userId = userContext.currentUserId();
        return doctorService.upsertAvailability(
                userId, new DoctorAvailabilityRequest(dayOfWeek, startTime, endTime, true));
    }
}
