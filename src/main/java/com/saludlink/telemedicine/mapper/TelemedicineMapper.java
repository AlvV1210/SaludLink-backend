package com.saludlink.telemedicine.mapper;

import com.saludlink.telemedicine.dto.ConsultationMessageResponse;
import com.saludlink.telemedicine.dto.TeleconsultJoinResponse;
import com.saludlink.telemedicine.model.ConsultationMessage;
import com.saludlink.telemedicine.model.TeleconsultSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TelemedicineMapper {

    @Mapping(target = "sessionId", source = "id")
    @Mapping(target = "appointmentId", source = "appointment.id")
    TeleconsultJoinResponse toJoinResponse(TeleconsultSession session);

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "senderUserId", source = "sender.id")
    @Mapping(
            target = "senderName",
            expression =
                    "java(message.getSender().getFirstName() + \" \" + message.getSender().getLastName())")
    ConsultationMessageResponse toMessageResponse(ConsultationMessage message);
}
