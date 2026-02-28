package com.matriz.backend.modules.courses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.matriz.backend.modules.professor.Professor;
import com.matriz.backend.modules.finance.holder.Holder;
import com.matriz.backend.modules.schedules.Schedule;
import com.matriz.backend.shared.enums.CourseType;
import com.matriz.backend.shared.enums.Tag;
import com.matriz.backend.shared.interfaces.PhotoOwner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements PhotoOwner {

    @Id
    @UuidGenerator
    @Column(unique = true, nullable = false)
    private UUID id;

    /* Mandatory fields */

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseType type;

    @Column(nullable = false)
    private String code;

    /* Optional fields */

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id")
    @JsonBackReference("professor-courses")
    private Professor professor;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id")
    @JsonBackReference("holder-courses")
    private Holder holder;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("course-schedules")
    private Set<Schedule> schedules = new LinkedHashSet<>();


    @Enumerated(EnumType.STRING)
    private Tag tag;

    /* Text fields */

    private String subtitle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> textList;
}
