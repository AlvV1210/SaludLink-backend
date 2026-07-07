package com.saludlink.mentalhealth.service;

import com.saludlink.auth.model.User;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningRequest;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningResponse;

public interface IMentalHealthService {

    MentalHealthScreeningResponse submit(User user, MentalHealthScreeningRequest request);

    MentalHealthScreeningResponse findLatestByUser(User user);
}
