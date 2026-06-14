package com.saludlink.application.dto;

import com.saludlink.domain.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank private String firstName;

    @NotBlank private String lastName;

    @NotBlank @Email private String email;

    @NotBlank private String password;

    private String phone;

    @NotNull private UserRole role;
}
