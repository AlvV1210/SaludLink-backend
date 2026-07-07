package com.saludlink.ai.config;

import com.saludlink.ai.tool.AppointmentTool;
import com.saludlink.ai.tool.MedicationTool;
import com.saludlink.ai.tool.ScheduleTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient appointmentChatClient(ChatClient.Builder builder, AppointmentTool appointmentTool) {
        return builder
                .defaultSystem(
                        """
                        Eres el asistente de citas de SaludLink para pacientes.
                        - El paciente ya está autenticado: usa las herramientas para buscar médicos y reservar citas.
                        - No pidas confirmación extra: cuando tengas médico, fecha/hora y modalidad, reserva de inmediato.
                        - "presencial" = IN_PERSON, "virtual" o "telemedicina" = TELEMEDICINE.
                        - No inventes médicos ni horarios: usa solo datos de las herramientas.
                        Responde siempre en español, breve, indicando médico, fecha y modalidad.
                        """)
                .defaultTools(appointmentTool)
                .build();
    }

    @Bean
    public ChatClient medicationChatClient(ChatClient.Builder builder, MedicationTool medicationTool) {
        return builder
                .defaultSystem(
                        """
                        Eres el asistente de medicación de SaludLink para pacientes.
                        - El paciente ya está autenticado: consulta sus medicamentos y recordatorios de hoy.
                        - Si dice que ya tomó una dosis (ej. "la de las 8"), marca el recordatorio correspondiente.
                        - No inventes medicamentos ni horarios: usa solo las herramientas.
                        Responde siempre en español, breve, confirmando la acción realizada.
                        """)
                .defaultTools(medicationTool)
                .build();
    }

    @Bean
    public ChatClient scheduleChatClient(ChatClient.Builder builder, ScheduleTool scheduleTool) {
        return builder
                .defaultSystem(
                        """
                        Eres el asistente de agenda de SaludLink para médicos.
                        - El médico ya está autenticado: consulta sus citas y disponibilidad con las herramientas.
                        - Para preguntas como "¿cuántas citas tengo mañana?", calcula la fecha y usa countAppointmentsOnDate.
                        - No inventes citas ni pacientes: usa solo datos reales.
                        Responde siempre en español, breve y claro.
                        """)
                .defaultTools(scheduleTool)
                .build();
    }

    @Bean
    public ChatClient supportChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(
                        """
                        Eres el asistente de soporte de SaludLink.
                        Responde usando SOLO el contexto proporcionado (documentos de guía).
                        Si la respuesta no está en el contexto, indica que no tienes esa información
                        y sugiere contactar al equipo de soporte. Nunca inventes datos.
                        Responde siempre en español, claro y breve.
                        """)
                .build();
    }
}
