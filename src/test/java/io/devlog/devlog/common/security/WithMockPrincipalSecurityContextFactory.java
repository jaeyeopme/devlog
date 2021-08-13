package io.devlog.devlog.common.security;

import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithMockPrincipalSecurityContextFactory implements WithSecurityContextFactory<WithMockPrincipal> {

    @Override
    public SecurityContext createSecurityContext(WithMockPrincipal annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        PrincipalDetails details = PrincipalDetails.builder()
                .email(annotation.email())
                .password(annotation.password())
                .authorities(Stream.of(annotation.authorities()).collect(Collectors.toSet()))
                .enabled(annotation.enabled())
                .build();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));

        return context;
    }

}
