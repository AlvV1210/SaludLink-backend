package com.saludlink.service.impl;

import com.saludlink.model.dto.RegisterRequestDTO;
import com.saludlink.model.entity.Patient;
import com.saludlink.model.entity.User;
import com.saludlink.model.enums.UserRole;
import com.saludlink.repository.PatientRepository;
import com.saludlink.repository.UserRepository;
import com.saludlink.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        User user =
                User.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .phone(dto.getPhone())
                        .role(dto.getRole())
                        .build();
        User saved = userRepository.save(user);
        if (saved.getRole() == UserRole.PATIENT) {
            patientRepository.save(Patient.builder().user(saved).build());
        }
        return saved;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
