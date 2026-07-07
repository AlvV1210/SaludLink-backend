package com.saludlink.auth.service;

import com.saludlink.auth.dto.RegisterRequest;
import com.saludlink.auth.exception.EmailAlreadyExistsException;
import com.saludlink.auth.model.User;
import com.saludlink.auth.model.UserRole;
import com.saludlink.auth.repository.UserRepository;
import com.saludlink.doctor.exception.LicenseAlreadyExistsException;
import com.saludlink.doctor.model.Doctor;
import com.saludlink.doctor.repository.DoctorRepository;
import com.saludlink.patient.model.Patient;
import com.saludlink.patient.repository.PatientRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }
        User user =
                User.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .phone(request.phone())
                        .role(request.role())
                        .build();
        User saved = userRepository.save(user);
        if (request.role() == UserRole.PATIENT) {
            patientRepository.save(Patient.builder().user(saved).build());
        }
        if (request.role() == UserRole.DOCTOR) {
            String specialty = normalizeRequiredField(request.specialty(), "La especialidad es obligatoria.");
            String licenseNumber =
                    normalizeRequiredField(request.licenseNumber(), "La matricula es obligatoria.");
            BigDecimal consultationFee = requireConsultationFee(request.consultationFee());
            if (doctorRepository.existsByLicenseNumber(licenseNumber)) {
                throw new LicenseAlreadyExistsException();
            }
            doctorRepository.save(
                    Doctor.builder()
                            .user(saved)
                            .specialty(specialty)
                            .licenseNumber(licenseNumber)
                            .consultationFee(consultationFee)
                            .verified(true)
                            .build());
        }
        return saved;
    }

    private BigDecimal requireConsultationFee(BigDecimal consultationFee) {
        if (consultationFee == null || consultationFee.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La tarifa de consulta es obligatoria.");
        }
        return consultationFee;
    }

    private String normalizeRequiredField(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(message);
        }
        return value.trim();
    }
}
