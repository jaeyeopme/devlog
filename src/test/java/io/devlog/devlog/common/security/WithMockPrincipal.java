package io.devlog.devlog.common.security;

import io.devlog.devlog.fixture.UserFixture;
import io.devlog.devlog.user.domain.entity.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPrincipalSecurityContextFactory.class)
public @interface WithMockPrincipal {

    String email() default UserFixture.EMAIL;

    String password() default UserFixture.PASSWORD;

    Authority[] authorities() default {Authority.USER};

    boolean enabled() default true;

}
