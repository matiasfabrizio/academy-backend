package com.matriz.backend.modules.professor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record ProfessorReqDto(
        @NotBlank(message = "El nombre del profesor es obligatorio")
        @Schema(example = "John Doe", description = "Professor's full name")
        String name,

        @Schema(example = "Magister, PhD, etc.", description = "Professor's description")
        String description
) {
}