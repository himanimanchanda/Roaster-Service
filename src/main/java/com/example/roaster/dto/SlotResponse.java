package com.example.roaster.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SlotResponse(
        Long doctorId,
        Long organizationId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {}
