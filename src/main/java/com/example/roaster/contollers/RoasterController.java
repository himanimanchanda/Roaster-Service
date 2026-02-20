package com.example.roaster.contollers;


import com.example.roaster.dto.AvailabilityRequest;
import com.example.roaster.dto.SlotResponse;
import com.example.roaster.security.JWTUtil;
import com.example.roaster.service.RoasterService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/roaster")
public class RoasterController {
    private final JWTUtil jwt;
    private final RoasterService rse;

    //Constructor to initialize obj
    public RoasterController(RoasterService rse ,JWTUtil jwt){
        this.rse=rse;
        this.jwt=jwt;
    }
    @GetMapping("/getUser")
    public String getUser(){
        return "api hit is successfully";
    }
    @PostMapping("/createRules")

    public String createRules(@RequestBody AvailabilityRequest rules, HttpServletRequest request) {
        try {
            System.out.println("controller hit successfully");
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer"))
                throw new RuntimeException("missing or invalid header");

            String token = authHeader.substring(7);
            Claims claims = jwt.validateToken(token);
            Number orgIdNum = (Number) claims.get("orgId");
            Long orgId = orgIdNum.longValue();
            Long docId = Long.valueOf(claims.getSubject());
            System.out.println(docId);
            return rse.createRulesService(rules, orgId, docId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
      @PostMapping("/organization/{orgId}/doctor/{docId}/Slots")
      public ResponseEntity <List<SlotResponse>>getDoctorSlots(@PathVariable Long docId,@PathVariable Long orgId,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate date){
        List<SlotResponse> slots =rse.getSlotsForDoctor(orgId,docId,date);
        return ResponseEntity.ok(slots);
      }




}
