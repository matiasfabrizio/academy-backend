package com.matriz.backend.modules.security.auth;

import com.matriz.backend.security.appuser.AppUser;
import com.matriz.backend.security.appuser.AppUserRepository;
import com.matriz.backend.shared.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
public class AppUserPersistenceTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private AppUserRepository repository;


    @Test
    void shouldPersistNewGoogleUserWithIncompleteProfile() {
        AppUser user = AppUser.builder()
                .firstName("Matias Fabrizio")
                .lastName("Prado Rivas")
                .email("matias300704@gmail.com")
                .role(Role.ROLE_STUDENT)
                .build();

        AppUser saved = repository.save(user);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.isProfileComplete()).isFalse();
        assertThat(saved.getPassword()).isNull();
        assertThat(saved.getDni()).isNull();
        assertThat(saved.getPhoneNumber()).isNull();
    }

    @Test
    void shouldPersistNewRegularUser() {
        AppUser user = AppUser.builder()
                .firstName("Matias Fabrizio")
                .lastName("Prado Rivas")
                .email("matias300704@gmail.com")
                .role(Role.ROLE_STUDENT)
                .dni("123456789")
                .phoneNumber("753-881-4222")
                .password("password")
                .profileComplete(true)
                .build();

        AppUser saved = repository.save(user);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.isProfileComplete()).isTrue();
        assertThat(saved.getPassword()).isNotEmpty();
        assertThat(saved.getDni()).isNotEmpty();
        assertThat(saved.getPhoneNumber()).isNotEmpty();
        assertThat(saved.getGoogleId()).isNull();
    }
}
