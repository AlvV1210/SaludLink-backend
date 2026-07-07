package com.saludlink.doctor.mapper;

import com.saludlink.doctor.dto.DoctorAvailabilityResponse;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.doctor.model.Doctor;
import com.saludlink.doctor.model.DoctorAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    DoctorResponse toResponse(Doctor doctor);

    DoctorAvailabilityResponse toAvailabilityResponse(DoctorAvailability availability);
}
