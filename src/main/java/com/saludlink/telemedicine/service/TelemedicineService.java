package com.saludlink.telemedicine.service;

import com.saludlink.appointment.exception.AppointmentNotFoundException;
import com.saludlink.appointment.model.Appointment;
import com.saludlink.appointment.model.AppointmentModality;
import com.saludlink.appointment.repository.AppointmentRepository;
import com.saludlink.auth.model.User;
import com.saludlink.shared.exception.BusinessRuleException;
import com.saludlink.telemedicine.dto.ConsultationMessageResponse;
import com.saludlink.telemedicine.dto.SendConsultationMessageRequest;
import com.saludlink.telemedicine.dto.TeleconsultJoinResponse;
import com.saludlink.telemedicine.mapper.TelemedicineMapper;
import com.saludlink.telemedicine.model.ConsultationMessage;
import com.saludlink.telemedicine.model.TeleconsultSession;
import com.saludlink.telemedicine.repository.ConsultationMessageRepository;
import com.saludlink.telemedicine.repository.TeleconsultSessionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelemedicineService implements ITelemedicineService {

    private final AppointmentRepository appointmentRepository;
    private final TeleconsultSessionRepository sessionRepository;
    private final ConsultationMessageRepository messageRepository;
    private final TelemedicineMapper telemedicineMapper;

    @Value("${saludlink.telemedicine.base-url:https://meet.saludlink.app}")
    private String telemedicineBaseUrl;

    @Override
    @Transactional
    public TeleconsultJoinResponse joinSession(Long userId, Long appointmentId) {
        Appointment appointment = requireAccessibleAppointment(userId, appointmentId);
        validateTeleconsultWindow(appointment);
        if (appointment.getModality() != AppointmentModality.TELEMEDICINE) {
            throw new BusinessRuleException("La cita no es de modalidad telemedicina");
        }
        return sessionRepository
                .findByAppointmentId(appointmentId)
                .map(telemedicineMapper::toJoinResponse)
                .orElseGet(() -> createSession(appointment));
    }

    @Override
    @Transactional
    public ConsultationMessageResponse sendMessage(
            Long userId, Long appointmentId, SendConsultationMessageRequest request) {
        Appointment appointment = requireAccessibleAppointment(userId, appointmentId);
        validatePostConsultChatWindow(appointment);
        User sender = resolveSender(userId, appointment);
        ConsultationMessage message =
                ConsultationMessage.builder()
                        .appointment(appointment)
                        .sender(sender)
                        .message(request.message())
                        .build();
        return telemedicineMapper.toMessageResponse(messageRepository.save(message));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationMessageResponse> listMessages(Long userId, Long appointmentId) {
        requireAccessibleAppointment(userId, appointmentId);
        return messageRepository.findByAppointmentId(appointmentId).stream()
                .map(telemedicineMapper::toMessageResponse)
                .toList();
    }

    private TeleconsultJoinResponse createSession(Appointment appointment) {
        String roomToken = UUID.randomUUID().toString();
        String joinUrl = telemedicineBaseUrl + "/room/" + roomToken;
        LocalDateTime expiresAt = appointment.getAppointmentDate().plusHours(1);
        TeleconsultSession session =
                TeleconsultSession.builder()
                        .appointment(appointment)
                        .roomToken(roomToken)
                        .joinUrl(joinUrl)
                        .expiresAt(expiresAt)
                        .build();
        return telemedicineMapper.toJoinResponse(sessionRepository.save(session));
    }

    private Appointment requireAccessibleAppointment(Long userId, Long appointmentId) {
        Appointment appointment =
                appointmentRepository
                        .findDetailById(appointmentId)
                        .orElseThrow(AppointmentNotFoundException::new);
        Long patientUserId = appointment.getPatient().getUser().getId();
        Long doctorUserId = appointment.getDoctor().getUser().getId();
        if (!userId.equals(patientUserId) && !userId.equals(doctorUserId)) {
            throw new BusinessRuleException("No tienes acceso a esta consulta");
        }
        return appointment;
    }

    private User resolveSender(Long userId, Appointment appointment) {
        if (appointment.getPatient().getUser().getId().equals(userId)) {
            return appointment.getPatient().getUser();
        }
        return appointment.getDoctor().getUser();
    }

    private void validateTeleconsultWindow(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = appointment.getAppointmentDate().minusMinutes(15);
        LocalDateTime end = appointment.getAppointmentDate().plusHours(1);
        if (now.isBefore(start) || now.isAfter(end)) {
            throw new BusinessRuleException("La videoconsulta no esta disponible en este horario");
        }
    }

    private void validatePostConsultChatWindow(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = appointment.getAppointmentDate().plusDays(3);
        if (now.isBefore(appointment.getAppointmentDate()) || now.isAfter(end)) {
            throw new BusinessRuleException("El chat post-consulta no esta habilitado");
        }
    }
}
