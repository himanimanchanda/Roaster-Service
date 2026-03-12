package com.example.roster.service;

import com.example.roster.dto.AvailabilityRequest;
import com.example.roster.dto.UnavailabilityRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface DoctorRosterService {
    String createRulesService(AvailabilityRequest request, HttpServletRequest httpRequest);

    String createUnavailabilityRulesService(UnavailabilityRequest rules, HttpServletRequest httpRequest);
}
