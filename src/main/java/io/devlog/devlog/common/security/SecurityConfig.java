package io.devlog.devlog.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.devlog.devlog.post.controller.PostController.POST_API_URI;
import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable();

        http.formLogin()
                .usernameParameter("email")
                .loginProcessingUrl(USER_API_URI + "/login")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("SESSION");

        http.authorizeRequests(authorize -> authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
                .mvcMatchers(POST, USER_API_URI).anonymous()
                .mvcMatchers(GET, USER_API_URI + "/verify-token/**").anonymous()
                .mvcMatchers(GET, USER_API_URI + "/duplicate/**").anonymous()
                .mvcMatchers(GET, POST_API_URI + "/**").permitAll()
                .mvcMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated())
                .httpBasic();
    }

}
