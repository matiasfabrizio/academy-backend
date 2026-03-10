package com.matriz.backend.security.cookies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
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
        ResponseCookieBuilder builder = ResponseCookie.from(COOKIE_NAME, jwt)
                .httpOnly(true)
                .secure(security)
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .sameSite("Lax");

        if (cookieDomain != null && !cookieDomain.isBlank() && !cookieDomain.equals("localhost")) {
            builder.domain(cookieDomain);
        }

        return builder.build();
    }

    /**
     * Generates an empty, instantly expired cookie to wipe the user's session.
     */
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookieBuilder builder = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(security)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)          // 0 seconds immediately invalidates and removes the cookie
                .sameSite("Lax");

        if (cookieDomain != null && !cookieDomain.isBlank() && !cookieDomain.equals("localhost")) {
            builder.domain(cookieDomain);
        }

        return builder.build();
    }
}
