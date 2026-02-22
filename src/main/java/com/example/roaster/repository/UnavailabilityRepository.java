package com.example.roaster.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.roaster.entity.Unavailability;


public interface UnavailabilityRepository extends JpaRepository<Unavailability,Long>{

    Optional<Unavailability> findByDoctorIdAndDateAndOrganizationId(
            Long doctorId,
            LocalDate date,
            Long orgId
    );
}
