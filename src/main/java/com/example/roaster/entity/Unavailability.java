package com.example.roaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table
        (
                name = "unavailability",
                uniqueConstraints = {
                        @UniqueConstraint(columnNames = {"doctor_id", "date","organization_id"})
                }
        )
public class Unavailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "reason")
    private String reason;
//    LEAVE ,SURGERY, EMERGENCY

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 🔹 REQUIRED BY JPA
    public Unavailability() {
    }

}
