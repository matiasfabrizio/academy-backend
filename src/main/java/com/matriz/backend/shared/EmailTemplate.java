package com.matriz.backend.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplate {
    WELCOME("¡Bienvenido a matriz!", "welcome"),
    RESET("Reinicio de contraseña", "reset"),
    ENROLL("¡Registrado exitosamente!", "enrolled");

    private final String subject;
    private final String template;
}
