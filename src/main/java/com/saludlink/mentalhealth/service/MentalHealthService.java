package com.saludlink.mentalhealth.service;

import com.saludlink.auth.model.User;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningRequest;
import com.saludlink.mentalhealth.dto.MentalHealthScreeningResponse;
import com.saludlink.mentalhealth.model.MentalHealthScreening;
import com.saludlink.mentalhealth.repository.MentalHealthScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentalHealthService implements IMentalHealthService {

    private final MentalHealthScreeningRepository screeningRepository;

    @Override
    @Transactional
    public MentalHealthScreeningResponse submit(User user, MentalHealthScreeningRequest request) {
        int score = request.answers().stream().mapToInt(Integer::intValue).sum();
        String level;
        String recommendation;
        boolean referral;
        if (score <= 4) {
            level = "BAJO";
            recommendation = "Continua con autocuidado y recursos psicoeducativos";
            referral = false;
        } else if (score <= 8) {
            level = "MODERADO";
            recommendation = "Considera solicitar orientacion con un profesional";
            referral = true;
        } else {
            level = "ALTO";
            recommendation = "Derivacion recomendada a salud mental de forma prioritaria";
            referral = true;
        }
        MentalHealthScreening screening =
                MentalHealthScreening.builder()
                        .user(user)
                        .score(score)
                        .level(level)
                        .recommendation(recommendation)
                        .build();
        MentalHealthScreening saved = screeningRepository.save(screening);
        return new MentalHealthScreeningResponse(
                saved.getId(), score, level, recommendation, referral);
    }

    @Override
    @Transactional(readOnly = true)
    public MentalHealthScreeningResponse findLatestByUser(User user) {
        return screeningRepository
                .findTopByUser_IdOrderByCreatedAtDesc(user.getId())
                .map(
                        saved ->
                                new MentalHealthScreeningResponse(
                                        saved.getId(),
                                        saved.getScore(),
                                        saved.getLevel(),
                                        saved.getRecommendation(),
                                        !"BAJO".equals(saved.getLevel())))
                .orElse(null);
    }
}
