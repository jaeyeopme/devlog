package io.devlog.devlog.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class PrincipalDetails implements UserDetails {

    private String email;

    private String password;

    private Set<Authority> authorities;

    private boolean enabled;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
