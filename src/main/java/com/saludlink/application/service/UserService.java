package com.saludlink.application.service;

import com.saludlink.application.dto.RegisterRequestDTO;
import com.saludlink.domain.model.entity.User;
import java.util.Optional;

public interface UserService {

    User registerUser(RegisterRequestDTO dto);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
