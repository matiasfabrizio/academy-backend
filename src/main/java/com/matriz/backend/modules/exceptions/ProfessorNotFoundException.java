package com.matriz.backend.modules.exceptions;

public class ProfessorNotFoundException extends RuntimeException {
    public ProfessorNotFoundException(String message) {
        super(message);
    }
}
