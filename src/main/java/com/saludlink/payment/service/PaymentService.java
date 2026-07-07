package com.saludlink.payment.service;

import com.saludlink.appointment.exception.AppointmentNotFoundException;
import com.saludlink.appointment.model.Appointment;
import com.saludlink.appointment.model.AppointmentStatus;
import com.saludlink.appointment.repository.AppointmentRepository;
import com.saludlink.payment.dto.PaymentResponse;
import com.saludlink.payment.dto.ProcessPaymentRequest;
import com.saludlink.payment.exception.PaymentAlreadyExistsException;
import com.saludlink.payment.mapper.PaymentMapper;
import com.saludlink.payment.model.AppointmentPayment;
import com.saludlink.payment.model.PaymentStatus;
import com.saludlink.payment.repository.AppointmentPaymentRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import com.saludlink.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse payAppointment(
            Long userId, Long appointmentId, ProcessPaymentRequest request) {
        Appointment appointment =
                appointmentRepository
                        .findDetailById(appointmentId)
                        .orElseThrow(AppointmentNotFoundException::new);
        if (!appointment.getPatient().getUser().getId().equals(userId)) {
            throw new BusinessRuleException("No puedes pagar esta cita");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BusinessRuleException("No se puede pagar una cita cancelada");
        }
        if (paymentRepository.existsByAppointmentId(appointmentId)) {
            throw new PaymentAlreadyExistsException();
        }
        if (request.amount().signum() <= 0) {
            throw new BusinessRuleException("El monto debe ser mayor a cero");
        }
        AppointmentPayment payment =
                AppointmentPayment.builder()
                        .appointment(appointment)
                        .amount(request.amount())
                        .paymentMethod(request.paymentMethod())
                        .cardLast4(request.cardLast4())
                        .status(PaymentStatus.COMPLETED)
                        .receiptNumber("SL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .build();
        if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
        }
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByAppointmentId(Long appointmentId) {
        return paymentRepository
                .findByAppointmentId(appointmentId)
                .map(paymentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
    }
}
