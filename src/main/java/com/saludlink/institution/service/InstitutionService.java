package com.saludlink.institution.service;

import com.saludlink.appointment.model.AppointmentStatus;
import com.saludlink.appointment.repository.AppointmentRepository;
import com.saludlink.auth.exception.EmailAlreadyExistsException;
import com.saludlink.auth.model.User;
import com.saludlink.auth.model.UserRole;
import com.saludlink.auth.repository.UserRepository;
import com.saludlink.doctor.dto.CreateDoctorRequest;
import com.saludlink.doctor.dto.DoctorResponse;
import com.saludlink.doctor.exception.DoctorNotFoundException;
import com.saludlink.doctor.mapper.DoctorMapper;
import com.saludlink.doctor.model.Doctor;
import com.saludlink.doctor.repository.DoctorRepository;
import com.saludlink.doctor.service.IDoctorService;
import com.saludlink.institution.dto.AffiliatedDoctorRequest;
import com.saludlink.institution.dto.LinkAffiliatedDoctorRequest;
import com.saludlink.institution.dto.InstitutionBillingResponse;
import com.saludlink.institution.dto.InstitutionDashboardResponse;
import com.saludlink.institution.dto.InstitutionInvoiceResponse;
import com.saludlink.institution.dto.InstitutionReportResponse;
import com.saludlink.institution.dto.InstitutionResponse;
import com.saludlink.institution.dto.RegisterInstitutionRequest;
import com.saludlink.institution.exception.InstitutionNotFoundException;
import com.saludlink.institution.exception.InvalidRucException;
import com.saludlink.institution.exception.RucAlreadyExistsException;
import com.saludlink.institution.mapper.InstitutionMapper;
import com.saludlink.institution.model.Institution;
import com.saludlink.institution.model.InstitutionDoctor;
import com.saludlink.institution.repository.InstitutionDoctorRepository;
import com.saludlink.payment.model.AppointmentPayment;
import com.saludlink.payment.model.PaymentStatus;
import com.saludlink.payment.repository.AppointmentPaymentRepository;
import com.saludlink.institution.repository.InstitutionRepository;
import com.saludlink.shared.exception.BusinessRuleException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstitutionService implements IInstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionDoctorRepository institutionDoctorRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final IDoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final InstitutionMapper institutionMapper;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentPaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.06");

    @Override
    @Transactional
    public InstitutionResponse register(RegisterInstitutionRequest request) {
        if (!request.ruc().matches("\\d{11}")) {
            throw new InvalidRucException();
        }
        if (institutionRepository.existsByRuc(request.ruc())) {
            throw new RucAlreadyExistsException();
        }
        if (userRepository.existsByEmail(request.adminEmail())) {
            throw new EmailAlreadyExistsException();
        }
        User admin =
                User.builder()
                        .firstName(request.adminFirstName())
                        .lastName(request.adminLastName())
                        .email(request.adminEmail())
                        .password(passwordEncoder.encode(request.adminPassword()))
                        .phone(request.adminPhone())
                        .role(UserRole.INSTITUTION_ADMIN)
                        .build();
        User savedAdmin = userRepository.save(admin);
        Institution institution =
                Institution.builder()
                        .name(request.name())
                        .ruc(request.ruc())
                        .address(request.address())
                        .establishmentType(request.establishmentType())
                        .adminUser(savedAdmin)
                        .build();
        return institutionMapper.toResponse(institutionRepository.save(institution));
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionResponse getByAdminUserId(Long adminUserId) {
        return institutionMapper.toResponse(requireByAdmin(adminUserId));
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionBillingResponse billing(Long adminUserId) {
        Institution institution = requireByAdmin(adminUserId);
        List<AppointmentPayment> payments =
                paymentRepository.findByInstitutionId(institution.getId());

        BigDecimal totalIncome =
                payments.stream()
                        .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                        .map(AppointmentPayment::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingAmount =
                payments.stream()
                        .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                        .map(AppointmentPayment::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount =
                payments.stream().filter(p -> p.getStatus() == PaymentStatus.PENDING).count();

        BigDecimal commission = totalIncome.multiply(COMMISSION_RATE);

        List<InstitutionInvoiceResponse> invoices =
                payments.stream()
                        .limit(20)
                        .map(this::toInvoice)
                        .toList();

        return new InstitutionBillingResponse(
                totalIncome, commission, pendingAmount, pendingCount, invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionDashboardResponse dashboard(Long adminUserId) {
        Institution institution = requireByAdmin(adminUserId);
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        long today =
                appointmentRepository.countByInstitutionDoctorsAndDateBetween(
                        institution.getId(), start, end);
        long noShows =
                appointmentRepository.countByInstitutionDoctorsAndStatus(
                        institution.getId(), AppointmentStatus.NO_SHOW);
        return new InstitutionDashboardResponse(today, BigDecimal.valueOf(75.0), noShows, BigDecimal.valueOf(82.5));
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionReportResponse report(Long adminUserId, LocalDate from, LocalDate to) {
        Institution institution = requireByAdmin(adminUserId);
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        long total =
                appointmentRepository.countByInstitutionDoctorsAndDateBetween(
                        institution.getId(), start, end);
        long attended =
                appointmentRepository.countByInstitutionDoctorsAndStatusBetween(
                        institution.getId(), AppointmentStatus.COMPLETED, start, end);
        long cancelled =
                appointmentRepository.countByInstitutionDoctorsAndStatusBetween(
                        institution.getId(), AppointmentStatus.CANCELLED, start, end);
        long noShows =
                appointmentRepository.countByInstitutionDoctorsAndStatusBetween(
                        institution.getId(), AppointmentStatus.NO_SHOW, start, end);
        return new InstitutionReportResponse(from, to, total, attended, cancelled, noShows);
    }

    @Override
    @Transactional
    public DoctorResponse addAffiliatedDoctor(Long adminUserId, AffiliatedDoctorRequest request) {
        Institution institution = requireByAdmin(adminUserId);
        Doctor doctor =
                doctorService.createDoctorEntity(
                        new CreateDoctorRequest(
                                request.firstName(),
                                request.lastName(),
                                request.email(),
                                request.password(),
                                request.phone(),
                                request.specialty(),
                                request.licenseNumber(),
                                request.biography(),
                                null));
        institutionDoctorRepository.save(
                InstitutionDoctor.builder().institution(institution).doctor(doctor).build());
        return doctorMapper.toResponse(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse linkAffiliatedDoctor(
            Long adminUserId, LinkAffiliatedDoctorRequest request) {
        Institution institution = requireByAdmin(adminUserId);
        Doctor doctor =
                doctorRepository
                        .findDetailById(request.doctorId())
                        .orElseThrow(DoctorNotFoundException::new);
        if (institutionDoctorRepository
                .findByInstitutionIdAndDoctorId(institution.getId(), doctor.getId())
                .isPresent()) {
            throw new BusinessRuleException("Este medico ya esta afiliado a tu institucion.");
        }
        institutionDoctorRepository.save(
                InstitutionDoctor.builder().institution(institution).doctor(doctor).build());
        return doctorMapper.toResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> listAffiliatedDoctors(Long adminUserId) {
        Institution institution = requireByAdmin(adminUserId);
        return institutionDoctorRepository.findByInstitutionId(institution.getId()).stream()
                .map(InstitutionDoctor::getDoctor)
                .map(doctorMapper::toResponse)
                .toList();
    }

    private Institution requireByAdmin(Long adminUserId) {
        return institutionRepository
                .findByAdminUserId(adminUserId)
                .orElseThrow(InstitutionNotFoundException::new);
    }

    private InstitutionInvoiceResponse toInvoice(AppointmentPayment payment) {
        String concept = "Pago de consulta";
        if ("TELEMEDICINE".equalsIgnoreCase(payment.getPaymentMethod())) {
            concept = "Telemedicina";
        } else if (payment.getPaymentMethod() != null
                && payment.getPaymentMethod().toUpperCase().contains("SMS")) {
            concept = "SMS notificaciones";
        }

        String reference =
                payment.getReceiptNumber() != null
                        ? payment.getReceiptNumber()
                        : "Cita #" + payment.getAppointment().getId();

        String status =
                switch (payment.getStatus()) {
                    case COMPLETED -> "Pagada";
                    case PENDING -> "Pendiente";
                    case FAILED -> "Fallida";
                };

        return new InstitutionInvoiceResponse(
                concept, reference, payment.getAmount(), payment.getPaidAt(), status);
    }
}
