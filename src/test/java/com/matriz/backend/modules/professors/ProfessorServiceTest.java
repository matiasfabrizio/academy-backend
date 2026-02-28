package com.matriz.backend.modules.professors;

import com.matriz.backend.modules.images.ImageProcessingService;
import com.matriz.backend.modules.professor.Professor;
import com.matriz.backend.modules.professor.ProfessorRepository;
import com.matriz.backend.modules.professor.ProfessorService;
import com.matriz.backend.modules.professor.dto.ProfessorMapper;
import com.matriz.backend.modules.professor.dto.ProfessorReqDto;
import com.matriz.backend.modules.professor.dto.ProfessorResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepo;

    @Mock
    private ProfessorMapper professorMapper;

    @Mock
    private ImageProcessingService imageProcessingService;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    @DisplayName("Should create a professor successfully")
    void createProfessor_ShouldSucceed() throws IOException {
        ProfessorReqDto reqDto = new ProfessorReqDto("John Doe", "Masters in Description");
        MultipartFile file = mock(MultipartFile.class);
        Professor professor = new Professor();
        ProfessorResDto resDto = new ProfessorResDto(UUID.randomUUID(), "John Doe", "Description", "url");

        when(professorMapper.toEntity(any(ProfessorReqDto.class))).thenReturn(professor);
        when(imageProcessingService.processAndUpload(any(), anyString(), any())).thenReturn("url");
        when(professorRepo.save(any(Professor.class))).thenReturn(professor);
        when(professorMapper.toResDto(any(Professor.class))).thenReturn(resDto);

        ProfessorResDto result = professorService.createProfessor(reqDto, file);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        verify(professorRepo, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get professor by id")
    void getProfessorById_ShouldReturnProfessor() {
        UUID id = UUID.randomUUID();
        Professor professor = new Professor();
        ProfessorResDto resDto = new ProfessorResDto(id, "John Doe", "url", "Description");

        when(professorRepo.findById(id)).thenReturn(Optional.of(professor));
        when(professorMapper.toResDto(professor)).thenReturn(resDto);

        ProfessorResDto result = professorService.getProfessorById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    @DisplayName("Should get all professors")
    void getAllProfessors_ShouldReturnSet() {
        Professor professor = new Professor();
        ProfessorResDto resDto = new ProfessorResDto(UUID.randomUUID(), "John Doe", "url", "Description");

        when(professorRepo.findAll()).thenReturn(Collections.singletonList(professor));
        when(professorMapper.toResDto(professor)).thenReturn(resDto);

        Set<ProfessorResDto> result = professorService.getAllProfessors();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should update professor when photo is provided")
    void updateProfessor_WithPhoto_ShouldSucceed() throws IOException {
        UUID id = UUID.randomUUID();
        ProfessorReqDto reqDto = new ProfessorReqDto("Jane Doe", "New Description");
        MultipartFile file = mock(MultipartFile.class);
        Professor professor = new Professor();
        professor.setName("John Doe");
        professor.setPhotoUrl("old-url");

        when(professorRepo.findById(id)).thenReturn(Optional.of(professor));
        when(imageProcessingService.processAndUpload(any(), anyString(), any())).thenReturn("new-url");
        when(professorRepo.save(any(Professor.class))).thenReturn(professor);
        when(professorMapper.toResDto(any())).thenReturn(new ProfessorResDto(id, "Jane Doe", "new-url", "New Doc"));

        ProfessorResDto result = professorService.updateProfessorById(id, reqDto, file);

        assertNotNull(result);
        verify(imageProcessingService).deleteImage("old-url");
        verify(imageProcessingService).processAndUpload(file, "Jane Doe", true);
    }

    @Test
    @DisplayName("Should update professor without changing the photo")
    void updateProfessor_WithoutPhoto_ShouldSucceed() throws IOException {
        UUID id = UUID.randomUUID();
        ProfessorReqDto reqDto = new ProfessorReqDto("Jane Doe", "New Description");
        Professor professor = new Professor();
        professor.setName("John Doe");
        professor.setPhotoUrl("old-url");
        professor.setDescription("Old Description");

        when(professorRepo.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepo.save(any(Professor.class))).thenReturn(professor);
        when(professorMapper.toResDto(any())).thenReturn(new ProfessorResDto(id, "Jane Doe", "old-url", "New Description"));

        ProfessorResDto result = professorService.updateProfessorById(id, reqDto, null);

        assertNotNull(result);
        assertEquals("Jane Doe", professor.getName());
        assertEquals("New Description", professor.getDescription());

        verify(imageProcessingService).renameImage("old-url", "Jane Doe");
        verify(imageProcessingService, never()).processAndUpload(any(), any(), any());
        verify(imageProcessingService, never()).deleteImage(any());
        verify(professorRepo, times(1)).save(professor);
    }

    @Test
    @DisplayName("Should update professor but only the description")
    void updateProfessor_WithoutNameOrPhoto_ShouldSucceed() throws IOException {
        UUID id = UUID.randomUUID();
        ProfessorReqDto reqDto = new ProfessorReqDto("John Doe", "New Description");
        Professor professor = new Professor();
        professor.setName("John Doe");
        professor.setPhotoUrl("old-url");
        professor.setDescription("Old Description");

        when(professorRepo.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepo.save(any(Professor.class))).thenReturn(professor);
        when(professorMapper.toResDto(any())).thenReturn(new ProfessorResDto(id, "John Doe", "old-url", "New Description"));

        ProfessorResDto result = professorService.updateProfessorById(id, reqDto, null);

        assertNotNull(result);
        assertEquals("John Doe", professor.getName());
        assertEquals("New Description", professor.getDescription());

        verify(imageProcessingService, never()).renameImage(any(), any());
        verify(imageProcessingService, never()).processAndUpload(any(), any(), any());
        verify(imageProcessingService, never()).deleteImage(any());
        verify(professorRepo, times(1)).save(professor);
    }

    @Test
    @DisplayName("Should delete professor by id")
    void deleteProfessorById_ShouldSucceed() {
        UUID id = UUID.randomUUID();
        Professor professor = new Professor();
        professor.setPhotoUrl("url");

        when(professorRepo.findById(id)).thenReturn(Optional.of(professor));

        professorService.deleteProfessorById(id);

        verify(imageProcessingService).deleteImage("url");
        verify(professorRepo).deleteById(id);
    }
}
