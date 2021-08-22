package io.devlog.devlog.error.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostAccessDeniedException extends RuntimeException {

    public PostAccessDeniedException(Long userId) {
        log.error("Access denied request user id: [\"{}\"]", userId);
    }

}
