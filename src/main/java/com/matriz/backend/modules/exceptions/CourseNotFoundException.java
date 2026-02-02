package com.matriz.backend.modules.exceptions;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) { super(message); }
}
