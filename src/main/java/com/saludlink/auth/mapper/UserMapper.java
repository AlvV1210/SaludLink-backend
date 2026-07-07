package com.saludlink.auth.mapper;

import com.saludlink.auth.dto.AuthMeResponse;
import com.saludlink.auth.dto.AuthResponse;
import com.saludlink.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "token", source = "token")
    AuthResponse toAuthResponse(User user, String token);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "patientId", source = "patientId")
    @Mapping(target = "doctorId", source = "doctorId")
    @Mapping(target = "institutionId", source = "institutionId")
    AuthMeResponse toMeResponse(
            User user, Long patientId, Long doctorId, Long institutionId);
}
