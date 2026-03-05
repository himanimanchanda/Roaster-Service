package com.example.roaster.service.impl;

import com.example.roaster.dto.AvailabilityRequest;
import com.example.roaster.dto.SlotResponse;
import com.example.roaster.dto.UnavailabilityRequest;
import com.example.roaster.entity.Availability;
import com.example.roaster.entity.Unavailability;
import com.example.roaster.repository.AvailaibilityRepository;
import com.example.roaster.repository.UnavailabilityRepository;
import com.example.roaster.security.JWTUtil;
import com.example.roaster.service.RoasterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RoasterServiceImpl implements RoasterService {

    private final AvailaibilityRepository rp;
    private final UnavailabilityRepository unp;
    private final JWTUtil jwt; // JWT injection for token parsing

    @Override
    public String createRulesService(AvailabilityRequest request, HttpServletRequest httpRequest) {
        Long orgId = getOrgIdFromToken(httpRequest);
        Long docId = getDocIdFromToken(httpRequest);

        for (DayOfWeek day : request.getDays()) {
            Availability avl = rp.findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId, docId, day)
                    .orElseGet(Availability::new);

            avl.setDoctorId(docId);
            avl.setOrganizationId(orgId);
            avl.setDayOfWeek(day);
            avl.setStartTime(request.getStartTime());
            avl.setEndTime(request.getEndTime());
            avl.setSlotDurationMin(request.getSlotDurationMinutes());
            avl.setActive(true);
            rp.save(avl);
        }
        return "Availability rules created successfully";
    }

    @Override
    public List<SlotResponse> getSlotsForDoctor(Long orgId, Long docId, LocalDate date) {
        unp.findByDoctorIdAndDateAndOrganizationId(docId, date, orgId)
                .ifPresent(unv -> {
                    throw new RuntimeException("Doctor is on leave on " + date + " because " + unv.getReason());
                });

        Availability availability = rp.findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId, docId, date.getDayOfWeek())
                .orElseThrow(() -> new RuntimeException("Doctor not available on this day"));

        return generateSlots(orgId, docId, date, availability.getStartTime(),
                availability.getEndTime(), availability.getSlotDurationMin());
    }

    @Override
    public String createUnavailabilityRulesService(UnavailabilityRequest rules, HttpServletRequest httpRequest) {
        Long orgId = getOrgIdFromToken(httpRequest);
        Long docId = getDocIdFromToken(httpRequest);

        for (LocalDate date : rules.getDates()) {
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Leaves can be applied for future dates only: " + date);
            }

            if (unp.findByDoctorIdAndDateAndOrganizationId(docId, date, orgId).isPresent()) {
                throw new RuntimeException("Leave already exists for " + date);
            }

            rp.findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId, docId, date.getDayOfWeek())
                    .orElseThrow(() -> new RuntimeException("You don't sit here on " + date.getDayOfWeek()));

            Unavailability unv = new Unavailability();
            unv.setOrganizationId(orgId);
            unv.setDoctorId(docId);
            unv.setDate(date);
            unv.setActive(true);
            unv.setReason(rules.getReason());
            unp.save(unv);
        }
        return "Leaves added successfully";
    }

    // --- Helper Methods for Token Parsing inside Service ---

    private Long getOrgIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ((Number) jwt.validateToken(token).get("orgId")).longValue();
    }

    private Long getDocIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return Long.valueOf(jwt.validateToken(token).getSubject());
    }

    private List<SlotResponse> generateSlots(Long orgId, Long docId, LocalDate date,
                                             LocalTime start, LocalTime end, int duration) {
        List<SlotResponse> slots = new ArrayList<>();
        LocalTime current = start;
        while (!current.plusMinutes(duration).isAfter(end)) {
            slots.add(new SlotResponse(docId, orgId, date, current, current.plusMinutes(duration)));
            current = current.plusMinutes(duration);
        }
        return slots;
    }
}