package com.saludlink.presentation.controller;

import com.saludlink.application.dto.AuthResponseDTO;
import com.saludlink.application.dto.LoginRequestDTO;
import com.saludlink.application.dto.RegisterRequestDTO;
import com.saludlink.application.service.UserService;
import com.saludlink.domain.model.entity.User;
import com.saludlink.infrastructure.security.CustomUserDetails;
import com.saludlink.infrastructure.security.JwtUtil;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {
        try {
            User user = userService.registerUser(dto);
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(toAuthResponse(token, user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            User user = principal.getUser();
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(toAuthResponse(token, user));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales incorrectas"));
        }
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
