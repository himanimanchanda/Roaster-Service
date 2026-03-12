package com.example.roster.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.roster.entity.Unavailability;


public interface UnavailabilityRepository extends JpaRepository<Unavailability,Long> {

    List<Unavailability> findByDoctorIdAndDateAndOrganizationId(
            Long doctorId,
            LocalDate date,
            Long organizationId
    );
}