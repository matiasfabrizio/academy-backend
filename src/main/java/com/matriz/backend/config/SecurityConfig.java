package com.matriz.backend.config;

import com.matriz.backend.security.jwt.JwtAuthenticationFilter;
import com.matriz.backend.security.oauth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final OAuth2LoginSuccessHandler oauth2SuccessHandler;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                // Use CORS config defined below
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF (we are using HttpOnly/SameSite cookies and JWTs)
                .csrf(AbstractHttpConfigurer::disable)

                // Set session to STATELESS (we don't want JSESSIONID)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // Define URL Access Rules (The Floor Plan)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/oauth2/**",
                                "/login/**",
                                "/error",
                                "/api/v1/courses",
                                "/api/v1/courses/{id}",
                                "/api/v1/courses/code/{code}",
                                "/api/v1/dev/**"
                        ).permitAll()                               // Allow for public usage.
                        .requestMatchers("/api/v1/registration/**").hasAuthority("ROLE_INCOMPLETE_PROFILE") // Make only incomplete users finish registration
                        .anyRequest().hasAuthority("ROLE_ADMIN")
                )

                // Configure OAuth2 Login (The Front Door)
                .oauth2Login(oauth -> oauth
                        .successHandler(oauth2SuccessHandler)
                )

                // Add the JWT Filter (The Hallway Monitor)
                // We put it BEFORE the standard login filter so it catches cookies first
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Removed the trailing slash on your production domain
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Crucial for your HttpOnly cookies!

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}