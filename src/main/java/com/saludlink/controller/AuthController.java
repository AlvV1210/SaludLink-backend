package com.saludlink.controller;

import com.saludlink.model.dto.AuthMeResponseDTO;
import com.saludlink.model.dto.AuthResponseDTO;
import com.saludlink.model.dto.LoginRequestDTO;
import com.saludlink.model.dto.RegisterRequestDTO;
import com.saludlink.model.entity.Doctor;
import com.saludlink.model.entity.Patient;
import com.saludlink.model.entity.User;
import com.saludlink.repository.DoctorRepository;
import com.saludlink.repository.PatientRepository;
import com.saludlink.security.CustomUserDetails;
import com.saludlink.security.JwtUtil;
import com.saludlink.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        User user = userService.registerUser(dto);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(toAuthResponse(token, user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getUser();
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(toAuthResponse(token, user));
    }

    /** Usuario autenticado actual (JWT). Sin token nuevo. */
    @GetMapping("/me")
    public ResponseEntity<AuthMeResponseDTO> me(@AuthenticationPrincipal CustomUserDetails principal) {
        User user = principal.getUser();
        Long patientId =
                patientRepository.findByUserId(user.getId()).map(Patient::getId).orElse(null);
        Long doctorId =
                doctorRepository.findByUserId(user.getId()).map(Doctor::getId).orElse(null);
        return ResponseEntity.ok(
                AuthMeResponseDTO.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .patientId(patientId)
                        .doctorId(doctorId)
                        .build());
    }

    private AuthResponseDTO toAuthResponse(String token, User user) {
        return AuthResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
