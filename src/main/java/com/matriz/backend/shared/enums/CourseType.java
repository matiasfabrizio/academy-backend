package com.matriz.backend.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseType {
    PRE(true),
    REFUERZO(true),
    ADMISION(false),
    ESCOLAR(false);

    private final boolean requiresQuote;
}
