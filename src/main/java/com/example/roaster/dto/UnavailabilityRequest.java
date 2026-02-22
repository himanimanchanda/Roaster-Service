package com.example.roaster.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Getter
@Setter
@Data
public class UnavailabilityRequest {

        private List<LocalDate> dates;
        private LocalTime startTime;
        private LocalTime endTime;
        private String reason;

        // getters & setters

}
