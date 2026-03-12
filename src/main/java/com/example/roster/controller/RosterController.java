package com.example.roster.controller;

import com.example.roster.dto.AvailabilityRequest;
import com.example.roster.dto.SlotResponse;
import com.example.roster.dto.UnavailabilityRequest;
import com.example.roster.service.RosterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/roster/user")
@RequiredArgsConstructor
public class RosterController {
    private final RosterService rosterservice;

    @GetMapping("/organisation/{orgId}/doctor/{docId}/slots")
    public ResponseEntity<List<SlotResponse>> getDoctorSlots(
            @PathVariable Long docId,
            @PathVariable Long orgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must be today or a future date");
        }
        return ResponseEntity.ok(rosterservice.getSlotsForDoctor(orgId, docId, date));
    }



//    Getting booked slots from booking microservice
@GetMapping("/booked-slots")
public List<SlotResponse> getBookedSlots(
        @RequestParam Long orgId,
        @RequestParam Long doctorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
){
    return rosterservice.getBookedSlots(orgId, doctorId, date.toString());
}
}