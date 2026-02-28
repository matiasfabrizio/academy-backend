package com.matriz.backend.modules.professor;

import com.matriz.backend.modules.professor.dto.ProfessorReqDto;
import com.matriz.backend.modules.professor.dto.ProfessorResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/professors")
@RequiredArgsConstructor
@Tag(name = "Professors", description = "Manage professors")
public class ProfessorController {
    private final ProfessorService professorService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Create a new professor", description = "Create a new professor with the provided information. Returns the created professor.")
    public ResponseEntity<ProfessorResDto> createProfessor(
            @RequestPart("professorReqDto")
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid ProfessorReqDto professorReqDto,
            @RequestPart MultipartFile photo
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professorService.createProfessor(professorReqDto, photo));
    }

    @GetMapping
    @Operation(summary = "List all professors", description = "Returns a list of all registered professors.")
    public ResponseEntity<Set<ProfessorResDto>> getAllProfessors() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific professor", description = "Returns the professor with the specified ID.")
    public ResponseEntity<ProfessorResDto> getProfessorById(@PathVariable UUID id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }

    @PatchMapping(path = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Update a specific professor", description = "Updates the professor with the specified ID.")
    public ResponseEntity<ProfessorResDto> updateProfessorById(
            @PathVariable UUID id,
            @RequestPart(name = "updatedProfessor")
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) ProfessorReqDto updatedProfessor,
            @RequestPart(name = "photo", required = false) MultipartFile photo
        ) throws IOException {

        professorService.updateProfessorById(id, updatedProfessor, photo);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific professor", description = "Deletes the professor with the specified ID.")
    public ResponseEntity<Void> deleteProfessorById(@PathVariable UUID id) {
        professorService.deleteProfessorById(id);
        return ResponseEntity.noContent().build();
    }
}
