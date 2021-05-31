package io.devlog.devlog.fixture;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRequest;

public class UserFixture {

    public static final User NEW_USER = User
            .builder()
            .email("newUser@email.com")
            .password("newUser1234!")
            .nickname("newUser")
            .build();

    public static final UserRequest USER_REGISTRATION_REQUEST = UserRequest
            .builder()
            .email("newUser@email.com")
            .password("newUser1234!")
            .nickname("newUser")
            .build();

}
