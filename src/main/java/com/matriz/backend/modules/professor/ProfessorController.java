package com.matriz.backend.modules.professor;

import com.matriz.backend.modules.professor.dto.ProfessorReqDto;
import com.matriz.backend.modules.professor.dto.ProfessorResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/professors")
@RequiredArgsConstructor
@Tag(name = "Professors", description = "Manage professors")
public class ProfessorController {
    private final ProfessorService professorService;

    @PostMapping
    @Operation(summary = "Create a new professor", description = "Create a new professor with the provided information. Returns the created professor.")
    public ResponseEntity<ProfessorResDto> create(@RequestBody @Valid ProfessorReqDto professorReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professorService.createProfessor(professorReqDto));
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

    @PutMapping("/{id}")
    @Operation(summary = "Update a specific professor", description = "Updates the professor with the specified ID.")
    public ResponseEntity<ProfessorResDto> updateProfessorById(@PathVariable UUID id, @RequestBody @Valid ProfessorReqDto updatedProfessor) {
        return ResponseEntity.ok(professorService.updateProfessorById(id, updatedProfessor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific professor", description = "Deletes the professor with the specified ID.")
    public ResponseEntity<Void> deleteProfessorById(@PathVariable UUID id) {
        professorService.deleteProfessorById(id);
        return ResponseEntity.noContent().build();
    }
}
