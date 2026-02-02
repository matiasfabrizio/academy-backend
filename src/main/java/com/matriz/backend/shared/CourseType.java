package com.matriz.backend.shared;

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
