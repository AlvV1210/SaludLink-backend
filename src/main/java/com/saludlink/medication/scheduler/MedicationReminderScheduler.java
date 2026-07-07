package com.saludlink.medication.scheduler;

import com.saludlink.medication.repository.MedicationReminderRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MedicationReminderScheduler {

    private final MedicationReminderRepository reminderRepository;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void markMissedReminders() {
        LocalDate today = LocalDate.now();
        LocalTime threshold = LocalTime.now().minusMinutes(30);
        reminderRepository.findMissedReminders(today, threshold).forEach(reminder -> {
            if (!reminder.isTaken() && "PENDING".equals(reminder.getStatus())) {
                reminder.setStatus("MISSED");
            }
        });
    }
}
