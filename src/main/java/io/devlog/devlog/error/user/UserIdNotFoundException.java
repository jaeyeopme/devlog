package io.devlog.devlog.error.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(Long id) {
        log.error("User id not found: [{}]", id);
    }

}
