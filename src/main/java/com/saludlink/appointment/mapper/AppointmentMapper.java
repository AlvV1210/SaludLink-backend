package com.saludlink.appointment.mapper;

import com.saludlink.appointment.dto.AppointmentResponse;
import com.saludlink.appointment.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(
            target = "doctorName",
            expression = "java(appointment.getDoctor().getUser().getFirstName() + \" \" + appointment.getDoctor().getUser().getLastName())")
    @Mapping(target = "specialty", source = "doctor.specialty")
    AppointmentResponse toResponse(Appointment appointment);
}
