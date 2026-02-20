package com.example.roaster.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AvailabilityRequest {

    private Long doctorId;
    private List<DayOfWeek> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;

    // getters & setters
}

