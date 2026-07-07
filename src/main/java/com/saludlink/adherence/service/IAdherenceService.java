package com.saludlink.adherence.service;

import com.saludlink.adherence.dto.AdherenceDashboardResponse;

public interface IAdherenceService {

    AdherenceDashboardResponse patientAdherence(Long patientId);
}
