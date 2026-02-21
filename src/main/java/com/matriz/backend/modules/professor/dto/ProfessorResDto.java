package com.matriz.backend.modules.professor.dto;

import java.util.UUID;

public record ProfessorResDto(
    UUID id,
    String name,
    String photoUrl,
    String description
) {}