package com.saludlink.payment.service;

import com.saludlink.payment.dto.PaymentResponse;
import com.saludlink.payment.dto.ProcessPaymentRequest;

public interface IPaymentService {

    PaymentResponse payAppointment(Long userId, Long appointmentId, ProcessPaymentRequest request);

    PaymentResponse getByAppointmentId(Long appointmentId);
}
