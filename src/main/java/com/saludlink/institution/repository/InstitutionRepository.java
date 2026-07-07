package com.saludlink.institution.repository;

import com.saludlink.institution.model.Institution;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    boolean existsByRuc(String ruc);

    @Query("SELECT i FROM Institution i WHERE i.adminUser.id = :userId")
    Optional<Institution> findByAdminUserId(@Param("userId") Long userId);
}
