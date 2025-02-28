package com.ies.ApplicationRegistrationMS.repository;

import com.ies.ApplicationRegistrationMS.entity.CitizenApplicationEntity;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitizenApplicationRepository extends JpaRepository<CitizenApplicationEntity, String> {

    List<CitizenApplicationEntity> findByCreatedBy(String userId);

    CitizenApplicationEntity findByCitizenEmail(@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{citizenEmail.pattern.invalid}") String citizenEmail);
}
