package com.matriz.backend.modules.courses;

import com.matriz.backend.modules.courses.dto.CourseReqDto;
import com.matriz.backend.modules.courses.dto.CourseResDto;
import com.matriz.backend.shared.CourseType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Manage academy courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Create a new course", description = "Create a new course with the provided information. Returns the created course.")
    public ResponseEntity<CourseResDto> create(@RequestBody @Valid CourseReqDto courseReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(courseReqDto));
    }

    @GetMapping
    @Operation(summary = "List all courses by type.", description = "List all courses by CourseType (Refuerzo, PRE, etc).")
    public ResponseEntity<Set<CourseResDto>> getAllCoursesByCourseType(@RequestParam(required = false) CourseType courseType) {
        if (courseType == null) {
            return ResponseEntity.ok(courseService.getAllCourses());
        }
        return ResponseEntity.ok(courseService.getAllCoursesByCourseType(courseType));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific course", description = "Returns the course with the specified ID.")
    public ResponseEntity<CourseResDto> getCourseById(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a specific course", description = "Updates the course with the specified ID.")
    public ResponseEntity<CourseResDto> updateCourseById(@RequestBody @Valid CourseReqDto updatedCourse, @PathVariable UUID id) {
        return ResponseEntity.ok(courseService.updateCourseById(updatedCourse, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific course", description = "Deletes the course with the specified ID.")
    public ResponseEntity<Void> deleteCourseById(@PathVariable UUID id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }
}
