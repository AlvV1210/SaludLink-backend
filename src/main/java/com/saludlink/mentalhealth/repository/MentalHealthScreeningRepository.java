package com.saludlink.mentalhealth.repository;

import com.saludlink.mentalhealth.model.MentalHealthScreening;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentalHealthScreeningRepository extends JpaRepository<MentalHealthScreening, Long> {

    Optional<MentalHealthScreening> findTopByUser_IdOrderByCreatedAtDesc(Long userId);
}
