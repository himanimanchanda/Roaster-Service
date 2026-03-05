package com.example.roaster.controller;

import com.example.roaster.dto.AvailabilityRequest;
import com.example.roaster.dto.SlotResponse;
import com.example.roaster.dto.UnavailabilityRequest;
import com.example.roaster.service.RoasterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/roaster")
@RequiredArgsConstructor
public class RoasterController {

    private final RoasterService rse;

    @PostMapping("/create/availability/rules")
    public String createAvailabilityRules(@RequestBody AvailabilityRequest rules, HttpServletRequest request) {
        return rse.createRulesService(rules, request);
    }

    @GetMapping("/organization/{orgId}/doctor/{docId}/Slots")
    public ResponseEntity<List<SlotResponse>> getDoctorSlots(
            @PathVariable Long docId,
            @PathVariable Long orgId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must be today or a future date");
        }
        return ResponseEntity.ok(rse.getSlotsForDoctor(orgId, docId, date));
    }

    @PostMapping("/create/unavailability/rules")
    public String createUnavailabilityRules(@RequestBody UnavailabilityRequest rules, HttpServletRequest request) {
        return rse.createUnavailabilityRulesService(rules, request);
    }
}