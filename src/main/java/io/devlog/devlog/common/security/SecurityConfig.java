package io.devlog.devlog.common.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.devlog.devlog.comment.controller.CommentController.COMMENT_API_URI;
import static io.devlog.devlog.post.controller.PostController.POST_API_URI;
import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console());
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
                .mvcMatchers(POST, USER_API_URI).permitAll()
                .mvcMatchers(USER_API_URI + "/verify-token/**").permitAll()
                .mvcMatchers(GET, USER_API_URI + "/duplicate/**").permitAll()
                .mvcMatchers(GET, POST_API_URI + "/**").permitAll()
                .mvcMatchers(GET, COMMENT_API_URI + "/**").permitAll()
                .anyRequest().authenticated())
                .httpBasic();
    }

}
