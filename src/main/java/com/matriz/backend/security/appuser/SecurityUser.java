package com.matriz.backend.security.appuser;

import com.matriz.backend.shared.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final AppUser appUser;

    @Override
    public String getUsername() {
        return appUser.getEmail();
    }

    @Override
    public String getPassword() {
        // Since you are using Google OAuth2 and JWTs, you might not have a password.
        // Returning null or an empty string is fine for stateless JWT setups
        // as long as you aren't using DaoAuthenticationProvider for password checking.
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If the profile is incomplete, force the restricted role.
        if (!appUser.isProfileComplete()) {
            return List.of(new SimpleGrantedAuthority(Role.ROLE_INCOMPLETE_PROFILE.name()));
        }

        // Otherwise, return their actual role (e.g., ROLE_STUDENT, ROLE_ADMIN)
        return List.of(new SimpleGrantedAuthority(appUser.getRole().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Optional: Expose the underlying AppUser if you ever need to extract
    // the UUID or DNI directly from the SecurityContext later.
    public AppUser getAppUser() {
        return this.appUser;
    }
}