package com.matriz.backend.security.registration;

import com.matriz.backend.security.appuser.SecurityUser;
import com.matriz.backend.security.registration.dto.RegisterProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/complete")
    public ResponseEntity<?> completeProfile(
            @RequestBody RegisterProfileRequest request,
            @AuthenticationPrincipal SecurityUser securityUser
        ) {

        String cookieString = registrationService.completeProfile(request, securityUser);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieString)
                .body("Perfil completado exitosamente.");
    }
}