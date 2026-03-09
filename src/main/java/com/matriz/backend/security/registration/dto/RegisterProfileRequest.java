package com.matriz.backend.security.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterProfileRequest (
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    String dni,

    @NotBlank(message = "El teléfono es obligatorio")
    String phoneNumber
) {}
