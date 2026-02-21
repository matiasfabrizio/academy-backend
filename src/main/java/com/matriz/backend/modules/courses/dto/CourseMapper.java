package com.matriz.backend.modules.courses.dto;

import com.matriz.backend.modules.courses.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseReqDto courseReqDto);

    @Mapping(source = "holder.id", target = "holderId")
    CourseResDto toResDto(Course course);
}
