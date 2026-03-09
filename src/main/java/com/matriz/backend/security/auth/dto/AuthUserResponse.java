package com.matriz.backend.security.auth.dto;

public record AuthUserResponse(
   String id,
   String firstName,
   String lastName,
   String email,
   String dni,
   String phoneNumber,
   String role
) {}
