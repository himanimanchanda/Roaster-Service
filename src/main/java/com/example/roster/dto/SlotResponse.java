package com.example.roster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class SlotResponse {

    private Long doctorId;
    private Long organizationId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

}