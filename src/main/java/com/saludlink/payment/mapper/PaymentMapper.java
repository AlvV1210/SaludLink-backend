package com.saludlink.payment.mapper;

import com.saludlink.payment.dto.PaymentResponse;
import com.saludlink.payment.model.AppointmentPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "appointmentId", source = "appointment.id")
    PaymentResponse toResponse(AppointmentPayment payment);
}
