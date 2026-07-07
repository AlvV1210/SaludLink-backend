package com.saludlink.review.mapper;

import com.saludlink.review.dto.ReviewResponse;
import com.saludlink.review.model.DoctorReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(
            target = "patientName",
            expression =
                    "java(review.getPatient().getUser().getFirstName() + \" \" + review.getPatient().getUser().getLastName())")
    ReviewResponse toResponse(DoctorReview review);
}
