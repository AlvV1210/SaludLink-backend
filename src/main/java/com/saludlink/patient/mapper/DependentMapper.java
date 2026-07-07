package com.saludlink.patient.mapper;

import com.saludlink.patient.dto.DependentResponse;
import com.saludlink.patient.model.DependentProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DependentMapper {

    @Mapping(target = "guardianPatientId", source = "guardian.id")
    DependentResponse toResponse(DependentProfile dependent);
}
