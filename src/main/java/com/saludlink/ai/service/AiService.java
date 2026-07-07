package com.saludlink.ai.service;

import com.saludlink.adherence.dto.AdherenceDashboardResponse;
import com.saludlink.adherence.service.IAdherenceService;
import com.saludlink.ai.dto.AdherenceInsight;
import com.saludlink.ai.dto.AdherenceReportResponse;
import com.saludlink.ai.dto.ReportInsight;
import com.saludlink.ai.dto.ReportResponse;
import com.saludlink.ai.dto.WellnessInsight;
import com.saludlink.ai.dto.WellnessReportResponse;
import com.saludlink.auth.model.User;
import com.saludlink.institution.dto.InstitutionReportResponse;
import com.saludlink.institution.service.IInstitutionService;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningResponse;
import com.saludlink.mentalhealth.service.IMentalHealthService;
import com.saludlink.shared.exception.BusinessRuleException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient appointmentChatClient;
    private final ChatClient medicationChatClient;
    private final ChatClient scheduleChatClient;
    private final ChatModel chatModel;
    private final IInstitutionService institutionService;
    private final IAdherenceService adherenceService;
    private final IMentalHealthService mentalHealthService;

    public String appointment(String message) {
        return appointmentChatClient.prompt().user(message).call().content();
    }

    public String medication(String message) {
        return medicationChatClient.prompt().user(message).call().content();
    }

    public String schedule(String message) {
        return scheduleChatClient.prompt().user(message).call().content();
    }

    public ReportResponse institutionReport(Long adminUserId, LocalDate from, LocalDate to) {
        InstitutionReportResponse data = institutionService.report(adminUserId, from, to);

        ReportInsight insight =
                ChatClient.create(chatModel)
                        .prompt()
                        .system(
                                """
                                Eres un analista de SaludLink. Dado el reporte de asistencia institucional,
                                escribe un resumen breve y una recomendación operativa.
                                Usa SOLO estos datos; no inventes cifras. Escribe resumen y recomendacion en español.
                                """)
                        .user(data.toString())
                        .call()
                        .entity(ReportInsight.class);

        return new ReportResponse(data, insight);
    }

    public AdherenceReportResponse adherenceReport(Long patientId) {
        AdherenceDashboardResponse data = adherenceService.patientAdherence(patientId);

        AdherenceInsight insight =
                ChatClient.create(chatModel)
                        .prompt()
                        .system(
                                """
                                Eres un asistente clínico de SaludLink para médicos.
                                Analiza la adherencia del paciente y describe un patrón detectado y una sugerencia de seguimiento.
                                Usa SOLO los datos proporcionados; no inventes porcentajes ni diagnósticos.
                                Escribe en español. No reemplaces el juicio médico del profesional.
                                """)
                        .user(data.toString())
                        .call()
                        .entity(AdherenceInsight.class);

        return new AdherenceReportResponse(data, insight);
    }

    public WellnessReportResponse wellnessInsight(User user) {
        MentalHealthScreeningResponse data = mentalHealthService.findLatestByUser(user);
        if (data == null) {
            throw new BusinessRuleException(
                    "No hay cribado de salud mental previo. Completa el test antes de solicitar el insight.");
        }

        WellnessInsight insight =
                ChatClient.create(chatModel)
                        .prompt()
                        .system(
                                """
                                Eres un asistente de bienestar de SaludLink para pacientes.
                                Reformula el resultado del cribado con tono informativo y empático.
                                Usa SOLO los datos del cribado; no diagnostiques ni prescribas tratamiento.
                                El campo disclaimer DEBE indicar que no sustituye evaluación profesional.
                                Escribe en español.
                                """)
                        .user(data.toString())
                        .call()
                        .entity(WellnessInsight.class);

        return new WellnessReportResponse(data, insight);
    }
}
