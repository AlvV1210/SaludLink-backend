package com.saludlink.telemedicine.service;

import com.saludlink.telemedicine.dto.ConsultationMessageResponse;
import com.saludlink.telemedicine.dto.SendConsultationMessageRequest;
import com.saludlink.telemedicine.dto.TeleconsultJoinResponse;
import java.util.List;

public interface ITelemedicineService {

    TeleconsultJoinResponse joinSession(Long userId, Long appointmentId);

    ConsultationMessageResponse sendMessage(
            Long userId, Long appointmentId, SendConsultationMessageRequest request);

    List<ConsultationMessageResponse> listMessages(Long userId, Long appointmentId);
}
