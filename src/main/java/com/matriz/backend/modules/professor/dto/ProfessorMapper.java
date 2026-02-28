package com.matriz.backend.modules.professor.dto;

import com.matriz.backend.modules.professor.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    Professor toEntity(ProfessorReqDto professorReqDto);

    ProfessorResDto toResDto(Professor professor);
}
