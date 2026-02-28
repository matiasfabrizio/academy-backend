package com.matriz.backend.modules.professor;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.matriz.backend.modules.courses.Course;
import com.matriz.backend.shared.interfaces.PhotoOwner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor implements PhotoOwner {

    @Id
    @UuidGenerator
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String photoUrl;

    private String description;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("professor-courses")
    private Set<Course> courses = new LinkedHashSet<>();
}
