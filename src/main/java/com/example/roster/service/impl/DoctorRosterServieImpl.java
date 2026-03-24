package com.example.roster.service.impl;

import com.example.roster.client.BookingClient;
import com.example.roster.dto.AvailabilityRequest;
import com.example.roster.dto.UnavailabilityRequest;
import com.example.roster.entity.Availability;
import com.example.roster.entity.Unavailability;
import com.example.roster.repository.AvailaibilityRepository;
import com.example.roster.repository.UnavailabilityRepository;
import com.example.roster.security.JWTUtil;
import com.example.roster.service.DoctorRosterService;
import com.example.roster.service.RosterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class DoctorRosterServieImpl implements DoctorRosterService {
    private final AvailaibilityRepository availabilityRepository;
    private final UnavailabilityRepository unavailabilityRepository;
    private final JWTUtil jwtUtil;

    @Override
    public String createRulesService(AvailabilityRequest request, HttpServletRequest httprequest) {

        Long orgId = (Long) httprequest.getAttribute("orgId");
        Long docId = (Long) httprequest.getAttribute("userId");

        for (DayOfWeek day : request.getDays()) {
            Availability availability = availabilityRepository
                    .findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId, docId, day)
                    .orElseGet(Availability::new);

            availability.setDoctorId(docId);
            availability.setOrganizationId(orgId);
            availability.setDayOfWeek(day);
            availability.setStartTime(request.getStartTime());
            availability.setEndTime(request.getEndTime());
            availability.setSlotDurationMin(request.getSlotDurationMinutes());
            availability.setActive(true);

            availabilityRepository.save(availability);
        }

        return "Availability rules created successfully";
    }


    @Override
    public String createUnavailabilityRulesService(UnavailabilityRequest request,
                                                   HttpServletRequest httpRequest) {

        Long orgId = (Long) httpRequest.getAttribute("orgId");
        Long docId = (Long) httpRequest.getAttribute("userId");

        for (LocalDate date : request.getDates()) {

            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Leaves can be applied only for future dates: " + date);
            }

            availabilityRepository
                    .findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId, docId, date.getDayOfWeek())
                    .orElseThrow(() -> new RuntimeException(
                            "Doctor has no availability on " + date.getDayOfWeek()));

            if (request.getStartTime() != null &&
                    request.getEndTime() != null &&
                    request.getStartTime().isAfter(request.getEndTime())) {

                throw new IllegalArgumentException("Start time must be before end time");
            }

            Unavailability unv = new Unavailability();

            unv.setOrganizationId(orgId);
            unv.setDoctorId(docId);
            unv.setDate(date);
            unv.setStartTime(request.getStartTime());
            unv.setEndTime(request.getEndTime());
            unv.setActive(true);
            unv.setReason(request.getReason());

            unavailabilityRepository.save(unv);
        }
        return "Unavailability added successfully";
    }
}





//    private Long getOrgIdFromToken(HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7);
//        return ((Number) jwtUtil.validateToken(token).get("orgId")).longValue();
//    }
//
//    private Long getDocIdFromToken(HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7);
//        return Long.valueOf(jwtUtil.validateToken(token).getSubject());
//    }
//}

