package com.example.roster.controller;

import com.example.roster.dto.AvailabilityRequest;
import com.example.roster.dto.UnavailabilityRequest;
import com.example.roster.service.impl.DoctorRosterServieImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roster/doctor")
@RequiredArgsConstructor
public class DoctorRosterController {
    private final DoctorRosterServieImpl doctorRosterService;
    @PostMapping("/availability")
    public String createAvailabilityRules(@RequestBody AvailabilityRequest rules, HttpServletRequest request) {
        return doctorRosterService.createRulesService(rules, request);
    }

    @PostMapping("/unavailability")
    public String createUnavailabilityRules(@RequestBody UnavailabilityRequest rules, HttpServletRequest request) {
        return doctorRosterService.createUnavailabilityRulesService(rules, request);
    }
}
