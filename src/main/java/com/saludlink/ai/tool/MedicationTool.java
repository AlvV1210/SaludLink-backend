package com.saludlink.ai.tool;

import com.saludlink.ai.security.AiUserContext;
import com.saludlink.medication.dto.CreateMedicationRequest;
import com.saludlink.medication.dto.MedicationReminderResponse;
import com.saludlink.medication.dto.MedicationResponse;
import com.saludlink.medication.mapper.MedicationMapper;
import com.saludlink.medication.repository.MedicationReminderRepository;
import com.saludlink.medication.service.IMedicationReminderService;
import com.saludlink.medication.service.IMedicationService;
import com.saludlink.shared.exception.BusinessRuleException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicationTool {

    private final AiUserContext userContext;
    private final IMedicationService medicationService;
    private final IMedicationReminderService reminderService;
    private final MedicationReminderRepository reminderRepository;
    private final MedicationMapper medicationMapper;

    @Tool(description = "Lista los medicamentos activos del paciente autenticado.")
    public List<MedicationResponse> listMyMedications() {
        return medicationService.listByPatient(userContext.currentPatientId());
    }

    @Tool(description = "Registra un nuevo medicamento para el paciente autenticado.")
    public MedicationResponse addMedication(
            @ToolParam(description = "nombre del medicamento") String name,
            @ToolParam(description = "dosis, ej. 500 mg") String dosage,
            @ToolParam(description = "frecuencia, ej. cada 8 horas") String frequency) {
        Long patientId = userContext.currentPatientId();
        return medicationService.addMedication(
                patientId,
                new CreateMedicationRequest(name, dosage, frequency, LocalDate.now(), null));
    }

    @Tool(description = "Lista los recordatorios de medicación de hoy para el paciente autenticado.")
    public List<MedicationReminderResponse> getTodayReminders() {
        Long patientId = userContext.currentPatientId();
        return reminderRepository.findUpcomingForPatient(patientId, LocalDate.now()).stream()
                .map(medicationMapper::toReminderResponse)
                .toList();
    }

    @Tool(description = "Marca un recordatorio como tomado por su id.")
    public MedicationReminderResponse markReminderTaken(
            @ToolParam(description = "id del recordatorio") Long reminderId) {
        return reminderService.markTaken(reminderId, userContext.currentPrincipal());
    }

    @Tool(description = "Marca como tomado el recordatorio de hoy que coincide con la hora indicada (ej. 08:00).")
    public MedicationReminderResponse markReminderTakenByTime(
            @ToolParam(description = "hora en formato HH:mm") String time) {
        LocalTime target = LocalTime.parse(time.trim());
        var match =
                getTodayReminders().stream()
                        .filter(r -> !r.taken() && r.scheduledTime().equals(target))
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new BusinessRuleException(
                                                "No hay recordatorio pendiente hoy a las " + time));
        return reminderService.markTaken(match.id(), userContext.currentPrincipal());
    }
}
