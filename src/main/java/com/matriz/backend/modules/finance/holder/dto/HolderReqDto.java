package com.matriz.backend.modules.finance.holder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HolderReqDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Schema(example = "Ruben Prado")
        String name,

        @NotNull(message = "El número de teléfono es obligatorio")
        @Schema(example = "996300605")
        String phoneNumber
) {}