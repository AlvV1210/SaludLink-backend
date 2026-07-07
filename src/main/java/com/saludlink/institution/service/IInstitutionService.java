package com.saludlink.institution.service;

import com.saludlink.institution.dto.AffiliatedDoctorRequest;
import com.saludlink.institution.dto.LinkAffiliatedDoctorRequest;
import com.saludlink.institution.dto.InstitutionBillingResponse;
import com.saludlink.institution.dto.InstitutionDashboardResponse;
import com.saludlink.institution.dto.InstitutionReportResponse;
import com.saludlink.institution.dto.InstitutionResponse;
import com.saludlink.institution.dto.RegisterInstitutionRequest;
import com.saludlink.doctor.dto.DoctorResponse;
import java.time.LocalDate;
import java.util.List;

public interface IInstitutionService {

    InstitutionResponse register(RegisterInstitutionRequest request);

    InstitutionResponse getByAdminUserId(Long adminUserId);

    InstitutionBillingResponse billing(Long adminUserId);

    InstitutionDashboardResponse dashboard(Long adminUserId);

    InstitutionReportResponse report(Long adminUserId, LocalDate from, LocalDate to);

    DoctorResponse addAffiliatedDoctor(Long adminUserId, AffiliatedDoctorRequest request);

    DoctorResponse linkAffiliatedDoctor(Long adminUserId, LinkAffiliatedDoctorRequest request);

    List<DoctorResponse> listAffiliatedDoctors(Long adminUserId);
}
