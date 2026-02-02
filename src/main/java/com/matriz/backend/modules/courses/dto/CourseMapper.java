package com.matriz.backend.modules.courses.dto;

import com.matriz.backend.modules.courses.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "holder", ignore = true)
    Course toEntity(CourseReqDto courseReqDto);

    @Mapping(source = "holder.id", target = "holderId")
    CourseResDto toResDto(Course course);
}
