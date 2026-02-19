package com.matriz.backend.modules.courses;

import com.matriz.backend.modules.courses.dto.CourseReqDto;
import com.matriz.backend.modules.courses.dto.CourseResDto;
import com.matriz.backend.modules.courses.dto.CourseMapper;
import com.matriz.backend.modules.exceptions.HolderNotFoundException;
import com.matriz.backend.modules.exceptions.CourseNotFoundException;
import com.matriz.backend.modules.exceptions.ProfessorNotFoundException;
import com.matriz.backend.modules.finance.holder.Holder;
import com.matriz.backend.modules.finance.holder.HolderRepository;
import com.matriz.backend.modules.professor.Professor;
import com.matriz.backend.modules.professor.ProfessorRepository;
import com.matriz.backend.modules.schedules.Schedule;
import com.matriz.backend.modules.schedules.dto.ScheduleMapper;
import com.matriz.backend.shared.CourseType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn =  Exception.class)
public class CourseService {
    private final CourseRepository courseRepo;
    private final HolderRepository holderRepo;
    private final ProfessorRepository professorRepo;
    private final CourseMapper courseMapper;
    private final ScheduleMapper scheduleMapper;
    
    public CourseResDto createCourse(CourseReqDto reqDto) {
        Course courseEntity = courseMapper.toEntity(reqDto);

        if (reqDto.professorId() != null) {
            Professor professor = professorRepo.findById(reqDto.professorId())
                    .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + reqDto.professorId()));
            courseEntity.setProfessor(professor);
        }

        if (reqDto.holderId() != null) {
            Holder holder = holderRepo.findById(reqDto.holderId())
                    .orElseThrow(() -> new HolderNotFoundException("Holder not found with id: " + reqDto.holderId()));
            courseEntity.setHolder(holder);
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

    public Set<CourseResDto> getAllCoursesByCourseType(CourseType courseType) {
        return courseRepo.findByType(courseType).stream()
                .map(courseMapper::toResDto)
                .collect(Collectors.toSet());
    }

    public CourseResDto getCourseById(UUID id) {
        return courseRepo.findById(id)
                .map(courseMapper::toResDto)
                .orElseThrow(() -> new CourseNotFoundException("Curso no encontrado con id: " + id));
    }

    public CourseResDto getCourseByCode(String code) {
        return courseRepo.findByCode(code)
                .map(courseMapper::toResDto)
                .orElseThrow(() -> new CourseNotFoundException("Curso no encontrado con código: " + code));
    }

    public CourseResDto updateCourseById(CourseReqDto reqDto, UUID id) {
        Course courseToUpdate = courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Curso no encontrado con id: " + id));

        // Set course fields
        courseToUpdate.setName(reqDto.name());
        courseToUpdate.setStartDate(reqDto.startDate());
        courseToUpdate.setEndDate(reqDto.endDate());
        courseToUpdate.setPhotoUrl(reqDto.photoUrl());
        courseToUpdate.setType(reqDto.type());
        courseToUpdate.setDescription(reqDto.description());
        courseToUpdate.setPrice(reqDto.price());
        courseToUpdate.setCode(reqDto.code());
        courseToUpdate.setTag(reqDto.tag());
        courseToUpdate.setSubtitle(reqDto.subtitle());
        courseToUpdate.setTextList(reqDto.textList());

        // Set Professor
        if (reqDto.professorId() != null) {
            if (courseToUpdate.getProfessor() == null || !courseToUpdate.getProfessor().getId().equals(reqDto.professorId())) {
                Professor professor = professorRepo.findById(reqDto.professorId())
                        .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + reqDto.professorId()));
                courseToUpdate.setProfessor(professor);
            }
        } else {
            courseToUpdate.setProfessor(null);
        }

        // Set Holder
        if (reqDto.holderId() != null) {
            if (courseToUpdate.getHolder() == null || !courseToUpdate.getHolder().getId().equals(reqDto.holderId())) {
                Holder holder = holderRepo.findById(reqDto.holderId())
                        .orElseThrow(() -> new HolderNotFoundException("Holder not found with id: " + reqDto.holderId()));
                courseToUpdate.setHolder(holder);
            }
        } else {
            courseToUpdate.setHolder(null);
        }

        // Set Schedules
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
