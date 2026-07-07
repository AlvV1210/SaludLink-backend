package com.saludlink.auth.service;

import com.saludlink.auth.dto.RegisterRequest;
import com.saludlink.auth.model.User;

public interface IUserService {

    User register(RegisterRequest request);
}
