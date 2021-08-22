package io.devlog.devlog.error.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDataDuplicationException extends RuntimeException {

    public UserDataDuplicationException() {
        log.error("User's some data is already existed");
    }

}
