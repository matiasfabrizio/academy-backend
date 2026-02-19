package com.matriz.backend.modules.courses;

import com.matriz.backend.modules.courses.dto.CourseMapper;
import com.matriz.backend.modules.courses.dto.CourseReqDto;
import com.matriz.backend.modules.courses.dto.CourseResDto;
import com.matriz.backend.modules.finance.holder.HolderRepository;
import com.matriz.backend.modules.schedules.dto.ScheduleMapper;
import com.matriz.backend.shared.CourseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("Should create a course successfully with mandatory fields")
    void createCourse_WithMandatoryFields_ShouldSucceed() {
        // Arrange
        CourseReqDto courseReqDto = new CourseReqDto(
                "Estadística 1",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 1),
                "https://photourl.com",
                CourseType.REFUERZO,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Course course = new Course();
        when(courseMapper.toEntity(any(CourseReqDto.class))).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResDto(any(Course.class))).thenReturn(new CourseResDto(
                UUID.randomUUID(),
                "Estadística 1",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));

        // Act
        CourseResDto result = courseService.createCourse(courseReqDto);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should create a REFUERZO course")
    void createCourse_WithRefuerzoTypeFields_ShouldSucceed() {

    }
}
