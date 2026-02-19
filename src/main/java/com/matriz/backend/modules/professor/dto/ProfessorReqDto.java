package com.matriz.backend.modules.professor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ProfessorReqDto(
        @NotBlank(message = "El nombre del profesor es obligatorio")
        @Schema(example = "John Doe", description = "Professor's full name")
        String name,

        @NotBlank(message = "La foto del profesor es obligatoria")
        @URL(message = "Debe ser una URL válida")
        @Schema(example = "https://cdn.matriz.edu/img/professors/john-doe.png", description = "URL to the professor's photo")
        String photoUrl
) {
}