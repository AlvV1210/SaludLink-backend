package com.saludlink.auth.service;

import com.saludlink.auth.dto.AuthMeResponse;
import com.saludlink.auth.dto.AuthResponse;
import com.saludlink.auth.dto.LoginRequest;
import com.saludlink.auth.dto.RegisterRequest;
import com.saludlink.auth.exception.AccountLockedException;
import com.saludlink.auth.mapper.UserMapper;
import com.saludlink.auth.model.User;
import com.saludlink.auth.repository.UserRepository;
import com.saludlink.auth.security.JwtService;
import com.saludlink.doctor.repository.DoctorRepository;
import com.saludlink.institution.repository.InstitutionRepository;
import com.saludlink.patient.repository.PatientRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_MINUTES = 15;

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final InstitutionRepository institutionRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user.getEmail());
        return userMapper.toAuthResponse(user, token);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user =
                userRepository
                        .findByEmail(request.email())
                        .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new LockedException("Cuenta bloqueada por intentos fallidos");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
            userRepository.save(user);
            String token = jwtService.generateToken(user.getEmail());
            return userMapper.toAuthResponse(user, token);
        } catch (BadCredentialsException ex) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
            }
            userRepository.save(user);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                throw new AccountLockedException();
            }
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthMeResponse me(User user) {
        Long patientId = patientRepository.findByUserId(user.getId()).map(p -> p.getId()).orElse(null);
        Long doctorId = doctorRepository.findByUserId(user.getId()).map(d -> d.getId()).orElse(null);
        Long institutionId =
                institutionRepository.findByAdminUserId(user.getId()).map(i -> i.getId()).orElse(null);
        return userMapper.toMeResponse(user, patientId, doctorId, institutionId);
    }
}
