package io.devlog.devlog.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static org.springframework.http.HttpMethod.GET;

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
                .cors().disable()
                .headers().frameOptions().disable();

        http
                .formLogin()
                .usernameParameter("email")
                .loginProcessingUrl(USER_API_URI + "/login")
                .and()
                .authorizeRequests()
                .antMatchers(GET, USER_API_URI + "/duplicated/**")
                .permitAll()
                .antMatchers(GET, USER_API_URI + "/verify-token/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, USER_API_URI)
                .permitAll()
                .antMatchers("/", "/h2-console/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

}
