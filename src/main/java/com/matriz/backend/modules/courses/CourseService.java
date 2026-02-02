package com.matriz.backend.modules.courses;

import com.matriz.backend.modules.courses.dto.CourseReqDto;
import com.matriz.backend.modules.courses.dto.CourseResDto;
import com.matriz.backend.modules.courses.dto.CourseMapper;
import com.matriz.backend.modules.exceptions.CourseNotFoundException;
import com.matriz.backend.modules.finance.holder.HolderRepository;
import com.matriz.backend.modules.schedules.Schedule;
import com.matriz.backend.modules.schedules.dto.ScheduleMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn =  Exception.class)
public class CourseService {
    private final CourseRepository courseRepo;
    private final HolderRepository holderRepo;
    private final CourseMapper courseMapper;
    private final ScheduleMapper scheduleMapper;
    
    public CourseResDto createCourse(CourseReqDto reqDto) {
        Course courseEntity = courseMapper.toEntity(reqDto);

        if (reqDto.holderId() != null) {
            courseEntity.setHolder(holderRepo.getReferenceById(reqDto.holderId()));
        }

        if (courseEntity.getSchedules() != null) {
            courseEntity.getSchedules().forEach(schedule -> schedule.setCourse(courseEntity));
        }

        Course savedCourse = courseRepo.save(courseEntity);

        return courseMapper.toResDto(savedCourse);
    }

    public Set<CourseResDto> getAllCourses() {
        return courseRepo.findAll().stream()
                .map(courseMapper::toResDto)
                .collect(Collectors.toSet());
    }

    public CourseResDto getCourseById(UUID id) {
        return courseRepo.findById(id)
                .map(courseMapper::toResDto)
                .orElseThrow(() -> new CourseNotFoundException("Curso no encontrado con id: " + id));
    }

    public CourseResDto updateCourseById(CourseReqDto reqDto, UUID id) {
        Course courseToUpdate = courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Curso no encontrado con id: " + id));

        courseToUpdate.setName(reqDto.name());
        courseToUpdate.setStartDate(reqDto.startDate());
        courseToUpdate.setEndDate(reqDto.endDate());
        courseToUpdate.setPhotoUrl(reqDto.photoUrl());
        courseToUpdate.setType(reqDto.type());
        courseToUpdate.setDescription(reqDto.description());
        courseToUpdate.setProfessor(reqDto.professor());
        courseToUpdate.setPrice(reqDto.price());
        courseToUpdate.setCode(reqDto.code());
        courseToUpdate.setTag(reqDto.tag());
        courseToUpdate.setSubtitle(reqDto.subtitle());
        courseToUpdate.setTextList(reqDto.textList());

        if (reqDto.holderId() != null) {
            if (courseToUpdate.getHolder() == null || !courseToUpdate.getHolder().getId().equals(reqDto.holderId())) {
                courseToUpdate.setHolder(holderRepo.getReferenceById(reqDto.holderId()));
            }
        } else {
            courseToUpdate.setHolder(null);
        }

        if (reqDto.schedules() != null) {
            courseToUpdate.getSchedules().clear();
            reqDto.schedules().forEach(schedule -> {
                Schedule newSchedule = scheduleMapper.toEntity(schedule);
                newSchedule.setCourse(courseToUpdate);
                courseToUpdate.getSchedules().add(newSchedule);
            });
        }

        Course savedCourse = courseRepo.save(courseToUpdate);

        return courseMapper.toResDto(savedCourse);
    }

    public void deleteCourseById(UUID id) {
        courseRepo.deleteById(id);
    }
}
