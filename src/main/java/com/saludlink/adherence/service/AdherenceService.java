package com.saludlink.adherence.service;

import com.saludlink.adherence.dto.AdherenceDashboardResponse;
import com.saludlink.medication.repository.MedicationReminderRepository;
import com.saludlink.patient.exception.PatientProfileNotFoundException;
import com.saludlink.patient.repository.PatientRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdherenceService implements IAdherenceService {

    private final PatientRepository patientRepository;
    private final MedicationReminderRepository reminderRepository;

    @Override
    @Transactional(readOnly = true)
    public AdherenceDashboardResponse patientAdherence(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientProfileNotFoundException();
        }
        var reminders =
                reminderRepository.findUpcomingForPatient(patientId, java.time.LocalDate.now()).stream()
                        .toList();
        long total = reminders.size();
        long taken = reminders.stream().filter(r -> r.isTaken() || "TAKEN".equals(r.getStatus())).count();
        if (total == 0) {
            return new AdherenceDashboardResponse(
                    patientId, BigDecimal.valueOf(100), "GREEN", 0, 0);
        }
        BigDecimal percent =
                BigDecimal.valueOf(taken * 100.0 / total).setScale(1, RoundingMode.HALF_UP);
        String semaphore = percent.compareTo(BigDecimal.valueOf(70)) >= 0 ? "GREEN" : "RED";
        return new AdherenceDashboardResponse(patientId, percent, semaphore, taken, total);
    }
}
