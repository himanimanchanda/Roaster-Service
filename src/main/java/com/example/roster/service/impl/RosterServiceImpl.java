package com.example.roster.service.impl;

import com.example.roster.client.BookingClient;
import com.example.roster.dto.AvailabilityRequest;
import com.example.roster.dto.SlotResponse;
import com.example.roster.dto.UnavailabilityRequest;
import com.example.roster.entity.Availability;
import com.example.roster.entity.Unavailability;
import com.example.roster.repository.AvailaibilityRepository;
import com.example.roster.repository.UnavailabilityRepository;
import com.example.roster.security.JWTUtil;
import com.example.roster.service.RosterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RosterServiceImpl implements RosterService {

    private final AvailaibilityRepository availabilityRepository;
    private final UnavailabilityRepository unavailabilityRepository;
    private final JWTUtil jwtUtil;
    private final BookingClient bookingClient;


    @Override
    public List<SlotResponse> getSlotsForDoctor(Long orgId, Long docId, LocalDate date) {

        Availability availability = availabilityRepository
                .findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId, docId, date.getDayOfWeek())
                .orElseThrow(() -> new RuntimeException(
                        "Doctor not available on " + date.getDayOfWeek()));


        List<SlotResponse> generatedSlots = generateSlots(  orgId, docId, date, availability.getStartTime(),
                availability.getEndTime(), availability.getSlotDurationMin());
        List<SlotResponse>bookedSlots=getBookedSlots(orgId,docId,date.toString());
        List<SlotResponse> available=removeBooked(generatedSlots,bookedSlots);
        return available;
    }

//    LIST OF BOOKED SLOTS- from booking microservice using feign client
    @Override
    public List<SlotResponse> getBookedSlots(Long orgId, Long docId, String date){

        List<SlotResponse> bookedSlots =
                bookingClient.getBookedSlots(orgId, docId, date);

        System.out.println("Booked slots: " + bookedSlots);

        return bookedSlots;
    }

    // SLOT GENERATION
    private List<SlotResponse> generateSlots(Long orgId,  Long docId,  LocalDate date, LocalTime start, LocalTime end,int duration) {
        List<SlotResponse> slots = new ArrayList<>();
        LocalTime current = start;

        while (!current.plusMinutes(duration).isAfter(end)) {
            slots.add(new SlotResponse(  docId,  orgId,date,current,current.plusMinutes(duration)));
            current = current.plusMinutes(duration);
        }
        return slots;
    }

    public List<SlotResponse> removeBooked(List<SlotResponse> generated,List<SlotResponse>booked){
        //set to store unique slots only;
        Set<String> bookedSet=new HashSet<>();
        List<SlotResponse> available=new ArrayList<>();

//        adding all slots into set the booked one then we will add it into final answer means the final answer

        for (SlotResponse b : booked) {
            bookedSet.add(b.getStartTime() + "_" + b.getEndTime());
        }
//        traversing through the generated one if the slot is not in set means is ti
        for(SlotResponse slot:generated){
            String key=slot.getStartTime()+"_"+slot.getEndTime();

            if(!bookedSet.contains(key)) available.add(slot);

        }

        return available;
    }
    // TOKEN    // HELPERS
    private Long getOrgIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ((Number) jwtUtil.validateToken(token).get("orgId")).longValue();
    }

    private Long getDocIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return Long.valueOf(jwtUtil.validateToken(token).getSubject());
    }
}