package com.matriz.backend.modules.courses;

import com.matriz.backend.modules.courses.dto.CourseMapper;
import com.matriz.backend.modules.courses.dto.CourseReqDto;
import com.matriz.backend.modules.courses.dto.CourseResDto;
import com.matriz.backend.shared.enums.CourseType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("Should create a course successfully with mandatory fields")
    void createCourse_WithMandatoryFields_ShouldSucceed() throws IOException {
        // Arrange
        CourseReqDto courseReqDto = new CourseReqDto(
                "Estadística 1",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 1),
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

        final String TINY_IMAGE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=";
        byte[] imageBytes = Base64.getDecoder().decode(TINY_IMAGE_BASE64);
        MultipartFile file = new MockMultipartFile("photo", "test.png", "image/png", imageBytes);
        log.info("Created image {} to S3",  file.getOriginalFilename());

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
        CourseResDto result = courseService.createCourse(courseReqDto, file);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should create a REFUERZO course")
    void createCourse_WithRefuerzoTypeFields_ShouldSucceed() {

    }
}
