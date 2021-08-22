package io.devlog.devlog.error.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidEmailTokenException extends RuntimeException {

    public InvalidEmailTokenException(String token) {
        log.error("Invalid email token: [\"{}\"]", token);
    }

}
