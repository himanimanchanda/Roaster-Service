package com.example.roaster.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
@Getter
@Setter
@Data
public class UnavailabilityRequest {

        private Long doctorId;
        private List<DayOfWeek> days;
        private LocalTime startTime;
        private LocalTime endTime;
        private String reason;

        // getters & setters

}
