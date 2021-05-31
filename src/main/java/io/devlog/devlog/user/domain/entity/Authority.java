package io.devlog.devlog.user.domain.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Authority implements GrantedAuthority {

    USER(Role.USER),
    ADMIN(Role.ADMIN);

    private final String authority;

    Authority(Role role) {
        this.authority = "ROLE_" + role;
    }

}
