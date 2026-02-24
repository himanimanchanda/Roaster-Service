package com.example.roaster.service;

import com.example.roaster.dto.SlotResponse;
import com.example.roaster.dto.UnavailabilityRequest;
import com.example.roaster.entity.Unavailability;
import com.example.roaster.repository.UnavailabilityRepository;
import org.springframework.stereotype.Service;
import com.example.roaster.dto.AvailabilityRequest;
import com.example.roaster.entity.Availability;
import com.example.roaster.repository.AvailaibilityRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoasterService {

    private final AvailaibilityRepository rp;
    private final UnavailabilityRepository unp;

    public RoasterService(AvailaibilityRepository rp, UnavailabilityRepository unp) {
        this.rp = rp;
        this.unp= unp;
    }

    public String createRulesService(AvailabilityRequest request,Long orgId,Long DocId) {
        for(int i =0;i< request.getDays().size();i++){
           DayOfWeek Day=request.getDays().get(i);
            Availability avl=rp.findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId,DocId,request.getDays().get(i)).orElseGet(Availability::new);
            avl.setDoctorId(DocId);
            avl.setOrganizationId(orgId);
            avl.setDayOfWeek(Day);
            avl.setStartTime(request.getStartTime());
            avl.setEndTime(request.getEndTime());
            avl.setSlotDurationMin(request.getSlotDurationMinutes());
            avl.setActive(true);
            rp.save(avl);
        }
  return "availability rules created successfully";
    }
//    This method will generate slots on run time
    public List<SlotResponse> generateSlotService(Long orgId ,Long docId,LocalDate date,LocalTime startTime,LocalTime endTime,int durationMin){
        List<SlotResponse> slots=new ArrayList<>();
        LocalTime current = startTime;
        while (!current.plusMinutes(durationMin).isAfter(endTime)){
            slots.add(new SlotResponse(docId,orgId,date,current,current.plusMinutes(durationMin)));
            current=current.plusMinutes(durationMin);
        }
        return slots;
    }
    public List<SlotResponse> getSlotsForDoctor(Long orgId,Long docId, LocalDate date) {
        Unavailability unv=unp.findByDoctorIdAndDateAndOrganizationId(docId, date, orgId).orElse(null);
        if(unv!=null &&  date.equals(unv.getDate())) {
            throw new RuntimeException("Doctor is on leave on "+date+" because "+unv.getReason());
        }
        Availability availability = rp.findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId,docId, date.getDayOfWeek()).orElseThrow(() ->
                new RuntimeException("doctor not available on this day"));

         return generateSlotService(
                 orgId,
                 docId,
                 date,
                 availability.getStartTime(),
                 availability.getEndTime(),
                 availability.getSlotDurationMin()
         );
    }
    public String createUnavailabilityRulesService(UnavailabilityRequest rules,long docId,long orgId){
        for(LocalDate date:rules.getDates()) {

            if(unp.findByDoctorIdAndDateAndOrganizationId(docId, date, orgId).isPresent()) throw new RuntimeException("Leaves already exists");
            Unavailability unv=unp.findByDoctorIdAndDateAndOrganizationId(docId, date, orgId)
                    .orElseGet(Unavailability::new);
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Leaves can be applied for future dates only");
            }
            Availability availability = rp.findByOrganizationIdAndDoctorIdAndDayOfWeek(orgId,docId, date.getDayOfWeek()).orElseThrow(() ->
                    new RuntimeException("you cant add leaves on "+ date +" because you don't sit here on " + date.getDayOfWeek()));

            unv.setOrganizationId(orgId);
            unv.setDoctorId(docId);
            unv.setDate(date);
            unv.setActive(true);
            unv.setReason(rules.getReason());
            unp.save(unv);
        }
        return "Leaves added successfully";

    }

}

