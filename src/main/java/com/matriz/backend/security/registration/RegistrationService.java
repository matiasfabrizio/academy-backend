package com.matriz.backend.security.registration;

import com.matriz.backend.security.appuser.AppUser;
import com.matriz.backend.security.appuser.AppUserRepository;
import com.matriz.backend.security.appuser.SecurityUser;
import com.matriz.backend.security.cookies.CookieService;
import com.matriz.backend.security.jwt.JwtService;
import com.matriz.backend.security.registration.dto.RegisterProfileRequest;
import com.matriz.backend.shared.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn =  Exception.class)
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public String completeProfile(RegisterProfileRequest request, SecurityUser securityUser) {
        AppUser user = securityUser.getAppUser();

        user.setDni(request.dni());
        user.setPhoneNumber(request.phoneNumber());
        user.setProfileComplete(true);
        user.setRole(Role.ROLE_STUDENT);

        appUserRepository.save(user);

        String newToken = jwtService.generateToken(securityUser, securityUser.getAppUser().isProfileComplete());
        return cookieService.generateJwtCookie(newToken).toString();
    }
}
