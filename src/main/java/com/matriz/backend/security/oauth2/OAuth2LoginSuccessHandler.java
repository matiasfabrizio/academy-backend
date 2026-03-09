package com.matriz.backend.security.oauth2;

import com.matriz.backend.security.appuser.AppUser;
import com.matriz.backend.security.appuser.AppUserRepository;
import com.matriz.backend.security.appuser.SecurityUser;
import com.matriz.backend.security.cookies.CookieService;
import com.matriz.backend.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final AppUserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. Extract the Google payload from the Authentication object
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleId = oAuth2User.getAttribute("sub");

        // 2. The Upsert Logic (Login vs. Registration)
        AppUser user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    // If they don't exist, this is a brand-new registration.
                    AppUser newUser = new AppUser();
                    newUser.setEmail(oAuth2User.getAttribute("email"));
                    newUser.setFirstName(oAuth2User.getAttribute("given_name"));
                    newUser.setLastName(oAuth2User.getAttribute("family_name"));
                    newUser.setGoogleId(googleId);
                    return userRepository.save(newUser);
                });

        // 3. Wrap the database user in our Adapter
        SecurityUser securityUser = new SecurityUser(user);

        // 4. Generate the JWT and package it into the HttpOnly Cookie
        String token = jwtService.generateToken(securityUser, securityUser.getAppUser().isProfileComplete());
        String cookieString = cookieService.generateJwtCookie(token).toString();

        // 5. Attach the cookie to the response header
        response.addHeader(HttpHeaders.SET_COOKIE, cookieString);

        // 6. Redirect the user back to your Next.js application
        if (user.isProfileComplete()) {
            response.sendRedirect(frontendUrl + "/perfil");
        } else {
            response.sendRedirect(frontendUrl + "/registro/completar");
        }
    }
}
