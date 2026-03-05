package com.example.roaster.service;


import com.example.roaster.dto.AvailabilityRequest;
import com.example.roaster.dto.SlotResponse;
import com.example.roaster.dto.UnavailabilityRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

public interface RoasterService {
    String createRulesService(AvailabilityRequest request, HttpServletRequest httpRequest);
    List<SlotResponse> getSlotsForDoctor(Long orgId, Long docId, LocalDate date);
    String createUnavailabilityRulesService(UnavailabilityRequest rules, HttpServletRequest httpRequest);
}