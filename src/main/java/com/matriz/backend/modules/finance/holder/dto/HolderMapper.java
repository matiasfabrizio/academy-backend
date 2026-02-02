package com.matriz.backend.modules.finance.holder.dto;

import com.matriz.backend.modules.courses.Course;
import com.matriz.backend.modules.courses.dto.CourseMapper;
import com.matriz.backend.modules.finance.bankAccount.dto.BankAccountMapper;
import com.matriz.backend.modules.finance.holder.Holder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BankAccountMapper.class, CourseMapper.class})
public interface HolderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bankAccounts", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Holder toEntity(HolderReqDto dto);

    @Mapping(source = "courses", target = "coursesIds")
    HolderResDto toDto(Holder entity);

    default Set<UUID> coursesToCourseIds(Set<Course> courses) {
        if (courses == null) {
            return null;
        }
        return courses.stream().map(Course::getId).collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Holder toEntity(HolderUpdateReqDto dto);
}