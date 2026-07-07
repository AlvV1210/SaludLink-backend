package com.saludlink.patient.service;

import com.saludlink.patient.dto.CreateDependentRequest;
import com.saludlink.patient.dto.DependentResponse;
import java.util.List;

public interface IDependentService {

    List<DependentResponse> listForGuardian(Long userId);

    DependentResponse create(Long userId, CreateDependentRequest request);

    DependentResponse getById(Long userId, Long dependentId);

    void deactivate(Long userId, Long dependentId);
}
