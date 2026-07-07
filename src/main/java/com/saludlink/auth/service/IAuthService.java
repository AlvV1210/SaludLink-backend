package com.saludlink.auth.service;

import com.saludlink.auth.dto.AuthMeResponse;
import com.saludlink.auth.dto.AuthResponse;
import com.saludlink.auth.dto.LoginRequest;
import com.saludlink.auth.dto.RegisterRequest;
import com.saludlink.auth.model.User;

public interface IAuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthMeResponse me(User user);
}
