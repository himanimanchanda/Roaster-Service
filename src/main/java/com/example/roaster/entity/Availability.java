package com.example.roaster.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Getter
@Setter
@Entity
@Table

        (
        name = "availability",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"doctor_id", "day_of_week"})
        }
)
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "slot_duration_min", nullable = false)
    private Integer slotDurationMin;

    @Column(name = "is_active")
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 🔹 REQUIRED BY JPA
    public Availability() {
    }

//    // ===== GETTERS =====
//
//    public Long getId() {
//        return id;
//    }
//
//    public Long getDoctorId() {
//        return doctorId;
//    }
//
//    public Long getOrganizationId() {
//        return organizationId;
//    }
//
//    public DayOfWeek getDayOfWeek() {
//        return dayOfWeek;
//    }
//
//    public LocalTime getStartTime() {
//        return startTime;
//    }
//
//    public LocalTime getEndTime() {
//        return endTime;
//    }
//
//    public Integer getSlotDurationMin() {
//        return slotDurationMin;
//    }
//
//    public Boolean getActive() {
//        return active;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    // ===== SETTERS =====
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setDoctorId(Long doctorId) {
//        this.doctorId = doctorId;
//    }
//
//    public void setOrganizationId(Long organizationId) {
//        this.organizationId = organizationId;
//    }
//
//    public void setDayOfWeek(DayOfWeek dayOfWeek) {
//        this.dayOfWeek = dayOfWeek;
//    }
//
//    public void setStartTime(LocalTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public void setEndTime(LocalTime endTime) {
//        this.endTime = endTime;
//    }
//
//    public void setSlotDurationMin(Integer slotDurationMin) {
//        this.slotDurationMin = slotDurationMin;
//    }
//
//    public void setActive(Boolean active) {
//        this.active = active;
//    }
//
}
