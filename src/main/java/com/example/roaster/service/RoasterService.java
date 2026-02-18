package com.example.roaster.service;

import com.example.roaster.dto.SlotResponse;
import org.springframework.stereotype.Service;
import com.example.roaster.dto.AvailabilityRequest;
import com.example.roaster.entity.Availability;
import com.example.roaster.repository.RosterRepo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoasterService {

    private final RosterRepo rp;

    public RoasterService(RosterRepo rp) {
        this.rp = rp;
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
}