package com.example.roster.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.roster.entity.Availability;

import java.time.DayOfWeek;
import java.util.Optional;

public interface AvailaibilityRepository extends JpaRepository<Availability, Long> {

    Optional<Availability> findByOrganizationIdAndDoctorIdAndDayOfWeek(Long organizationId ,Long doctorId,DayOfWeek dayOfWeek);

}