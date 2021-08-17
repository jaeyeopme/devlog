package io.devlog.devlog.common.fixture;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRegisterRequest;
import io.devlog.devlog.user.dto.UserResponse;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFixture {
    public static final Long ID = 1L;
    public static final String EMAIL = "email1@email.com";
    public static final String PASSWORD = "Password1234!@";
    public static final String NICKNAME = "nickname1";
    public static final String EMAIL_TOKEN = "emailToken1";
    public static final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static final UserRegisterRequest REGISTER_REQUEST = UserRegisterRequest.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .nickname(NICKNAME)
            .build();
    public static final User USER = User.from(REGISTER_REQUEST, ENCODER);
    public static final UserResponse RESPONSE = UserResponse.from(USER);
    public static final UserUpdateRequest UPDATE_REQUEST = UserUpdateRequest.builder()
            .nickname(NICKNAME)
            .build();
}
