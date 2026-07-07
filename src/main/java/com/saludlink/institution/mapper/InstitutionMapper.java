package com.saludlink.institution.mapper;

import com.saludlink.institution.dto.InstitutionResponse;
import com.saludlink.institution.model.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    @Mapping(target = "adminUserId", source = "adminUser.id")
    InstitutionResponse toResponse(Institution institution);
}
