package com.example.roaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class Unavailability {

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

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "reason")
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 🔹 REQUIRED BY JPA
    public Unavailability() {
    }

}
