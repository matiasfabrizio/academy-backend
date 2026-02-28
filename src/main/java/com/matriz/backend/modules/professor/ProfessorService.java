package com.matriz.backend.modules.professor;

import com.matriz.backend.modules.exceptions.ProfessorNotFoundException;
import com.matriz.backend.modules.images.ImageProcessingService;
import com.matriz.backend.modules.professor.dto.ProfessorMapper;
import com.matriz.backend.modules.professor.dto.ProfessorReqDto;
import com.matriz.backend.modules.professor.dto.ProfessorResDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ProfessorService {
    private final ProfessorMapper professorMapper;
    private final ProfessorRepository professorRepo;
    private final ImageProcessingService imageProcessingService;

    public ProfessorResDto createProfessor(ProfessorReqDto professorReqDto, MultipartFile photo) throws IOException {
        Professor newProfessor = professorMapper.toEntity(professorReqDto);
        newProfessor.setPhotoUrl(imageProcessingService.processAndUpload(photo, professorReqDto.name(), true));
        return professorMapper.toResDto(professorRepo.save(newProfessor));
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

    public ProfessorResDto updateProfessorById(UUID id, ProfessorReqDto updatedProfessor, MultipartFile photo) throws IOException {
        Professor professorToUpdate = professorRepo.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + id));

        imageProcessingService.handlePhotoUpdate(professorToUpdate, photo, updatedProfessor.name(), true);

        professorToUpdate.setName(updatedProfessor.name());
        professorToUpdate.setDescription(updatedProfessor.description());

        return professorMapper.toResDto(professorRepo.save(professorToUpdate));
    }

    public void deleteProfessorById(UUID id) {
        Professor professor = professorRepo.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + id));
        imageProcessingService.deleteImage(professor.getPhotoUrl());
        professorRepo.deleteById(id);
    }

}
