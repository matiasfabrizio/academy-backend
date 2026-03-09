package com.matriz.backend.security.auth;

import com.matriz.backend.security.appuser.SecurityUser;
import com.matriz.backend.security.auth.dto.AuthUserMapper;
import com.matriz.backend.security.auth.dto.AuthUserResponse;
import com.matriz.backend.security.cookies.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserMapper authUserMapper;
    private final CookieService cookieService;

    @GetMapping("/me")
    public ResponseEntity<AuthUserResponse> getCurrentUser(@AuthenticationPrincipal SecurityUser securityUser) {
        return ResponseEntity.ok(authUserMapper.toDto(securityUser.getAppUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.getCleanJwtCookie().toString())
                .body("Sesión cerrada exitosamente");
    }
}
