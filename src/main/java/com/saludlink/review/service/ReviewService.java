package com.saludlink.review.service;

import com.saludlink.appointment.exception.AppointmentNotFoundException;
import com.saludlink.appointment.model.Appointment;
import com.saludlink.appointment.model.AppointmentStatus;
import com.saludlink.appointment.repository.AppointmentRepository;
import com.saludlink.review.dto.CreateReviewRequest;
import com.saludlink.review.dto.ReviewResponse;
import com.saludlink.review.exception.ReviewAlreadyExistsException;
import com.saludlink.review.mapper.ReviewMapper;
import com.saludlink.review.model.DoctorReview;
import com.saludlink.review.repository.DoctorReviewRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse create(Long userId, CreateReviewRequest request) {
        Appointment appointment =
                appointmentRepository
                        .findDetailById(request.appointmentId())
                        .orElseThrow(AppointmentNotFoundException::new);
        if (!appointment.getPatient().getUser().getId().equals(userId)) {
            throw new BusinessRuleException("No puedes resenar esta cita");
        }
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BusinessRuleException("Solo puedes resenar citas completadas");
        }
        if (reviewRepository.existsByAppointmentId(request.appointmentId())) {
            throw new ReviewAlreadyExistsException();
        }
        DoctorReview review =
                DoctorReview.builder()
                        .appointment(appointment)
                        .patient(appointment.getPatient())
                        .doctor(appointment.getDoctor())
                        .rating(request.rating())
                        .comment(request.comment())
                        .build();
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> listByDoctor(Long doctorId) {
        return reviewRepository.findByDoctorId(doctorId).stream()
                .map(reviewMapper::toResponse)
                .toList();
    }
}
