package io.devlog.devlog.common.security;

import io.devlog.devlog.user.domain.entity.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPrincipalSecurityContextFactory.class)
public @interface WithMockPrincipal {


    String email() default "email@email.com";

    String password() default "Password1234!";

    Authority[] authorities() default {Authority.USER};

    boolean enabled() default true;

}
