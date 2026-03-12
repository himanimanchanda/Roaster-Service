package com.example.roster.client;


import com.example.roster.dto.SlotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name="booking-service",
        url="http://localhost:8082"
)
public interface BookingClient {

    @GetMapping("/api/booking/booked-slots")
    List<SlotResponse> getBookedSlots(
            @RequestParam Long orgId,
            @RequestParam Long doctorId,
            @RequestParam String date
    );
}
