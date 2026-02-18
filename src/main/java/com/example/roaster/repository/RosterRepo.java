package com.example.roaster.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.roaster.entity.Availability;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface RosterRepo extends JpaRepository<Availability, Long> {

    Optional<Availability> findByOrganizationIdAndDoctorIdAndDayOfWeek(Long organizationId ,Long doctorId,DayOfWeek dayOfWeek);

}