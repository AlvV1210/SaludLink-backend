package com.saludlink.model.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Campos opcionales: solo se actualizan los enviados (no null). */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfileUpdateDTO {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 30)
    private String phone;

    private LocalDate birthDate;

    @Size(max = 10)
    private String bloodType;

    @Size(max = 2000)
    private String allergies;

    @Size(max = 5000)
    private String medicalHistory;

    @Size(max = 2000)
    private String insuranceInfo;
}
