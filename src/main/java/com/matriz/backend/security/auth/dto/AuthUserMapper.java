package com.matriz.backend.security.auth.dto;

import com.matriz.backend.security.appuser.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "googleId", ignore = true)
    AppUser toEntity(AuthUserResponse authUserResponse);

    AuthUserResponse toDto(AppUser appUser);
}
