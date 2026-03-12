package com.example.roster.service;


import com.example.roster.dto.AvailabilityRequest;
import com.example.roster.dto.SlotResponse;
import com.example.roster.dto.UnavailabilityRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

public interface RosterService {
    List<SlotResponse> getBookedSlots(Long orgId, Long docId, String date);
    List<SlotResponse> getSlotsForDoctor(Long orgId, Long docId, LocalDate date);

}