package com.matriz.backend.modules.professor;

import com.matriz.backend.modules.exceptions.ProfessorNotFoundException;
import com.matriz.backend.modules.professor.dto.ProfessorMapper;
import com.matriz.backend.modules.professor.dto.ProfessorReqDto;
import com.matriz.backend.modules.professor.dto.ProfessorResDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn =  Exception.class)
public class ProfessorService {
    private final ProfessorMapper professorMapper;
    private final ProfessorRepository professorRepo;

    public ProfessorResDto createProfessor(ProfessorReqDto professorReqDto) {
        return professorMapper.toResDto(professorRepo.save(professorMapper.toEntity(professorReqDto)));
    }

    public ProfessorResDto getProfessorById(UUID id) {
        return professorRepo.findById(id)
                .map(professorMapper::toResDto)
                .orElseThrow(() -> new RuntimeException("Professor not found with id: " + id));
    }

    public Set<ProfessorResDto> getAllProfessors() {
        return professorRepo.findAll().stream()
                .map(professorMapper::toResDto)
                .collect(Collectors.toSet());
    }

    public ProfessorResDto updateProfessorById(UUID id, ProfessorReqDto updatedProfessor) {
        Professor professor = professorRepo.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + id));
        professor.setName(updatedProfessor.name());
        professor.setPhotoUrl(updatedProfessor.photoUrl());
        professor.setDescription(updatedProfessor.description());

        return professorMapper.toResDto(professorRepo.save(professor));
    }

    public void deleteProfessorById(UUID id) {
        professorRepo.deleteById(id);
    }

}
