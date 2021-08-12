package io.devlog.devlog.error.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        log.error("User email not found: [{}]", email);
    }

}
