package com.matriz.backend.security.cookies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${app.cookie.security}")
    private boolean security;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    private static final String COOKIE_NAME = "access_token";

    /**
     * Wraps the minted JWT into a secure HTTP-Only cookie.
     */
    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from(COOKIE_NAME, jwt)
                .httpOnly(true)     // Prevents JavaScript (Next.js) from reading the cookie, blocking XSS attacks
                .secure(security)       // Ensures the cookie is only sent over HTTPS. **NOTE: Set to false if testing locally on HTTP**
                .domain(cookieDomain)
                .path("/")          // Makes the cookie accessible across the entire Matriz application
                .maxAge(jwtExpirationMs / 1000) // maxAge expects seconds, so we divide your YML's milliseconds by 1000
                .sameSite("Lax") // Blocks the cookie from being sent in cross-site requests, mitigating CSRF
                .build();
    }

    /**
     * Generates an empty, instantly expired cookie to wipe the user's session.
     */
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(security)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)          // 0 seconds immediately invalidates and removes the cookie
                .sameSite("Lax")
                .build();
    }
}
