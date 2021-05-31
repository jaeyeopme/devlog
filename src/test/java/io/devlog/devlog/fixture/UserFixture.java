package io.devlog.devlog.fixture;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRequest;

public class UserFixture {

    public static final UserRequest USER_REQUEST = UserRequest
            .builder()
            .email("userRequest@email.com")
            .password("userPass1234!")
            .nickname("userRequest")
            .build();

    public static final User USER = User
            .builder()
            .email("user@email.com")
            .password("userPass1234!")
            .nickname("user")
            .build();

    public static final String EMAIL_TOKEN = "emailToken";

}
