package com.saludlink.medication.mapper;

import com.saludlink.medication.dto.MedicationIntakeResponse;
import com.saludlink.medication.dto.MedicationReminderResponse;
import com.saludlink.medication.dto.MedicationResponse;
import com.saludlink.medication.model.Medication;
import com.saludlink.medication.model.MedicationIntake;
import com.saludlink.medication.model.MedicationReminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    @Mapping(target = "patientId", source = "patient.id")
    MedicationResponse toResponse(Medication medication);

    @Mapping(target = "medicationId", source = "medication.id")
    MedicationReminderResponse toReminderResponse(MedicationReminder reminder);

    @Mapping(target = "medicationId", source = "medication.id")
    MedicationIntakeResponse toIntakeResponse(MedicationIntake intake);
}
