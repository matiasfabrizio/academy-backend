package com.matriz.backend.modules.schedules;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.matriz.backend.modules.courses.Course;
import com.matriz.backend.modules.enrollments.Enrollment;
import com.matriz.backend.shared.Mode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference("course-schedules")
    private Course course;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int enrolledStudents = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Mode mode;

    /* Optional Fields */

    private int classNumber;

    private String classroom;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("schedule-enrollment")
    private Set<Enrollment> enrollments = new HashSet<>();
}
