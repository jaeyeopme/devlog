package io.devlog.devlog.error.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        log.error("Post id not found: [\"{}\"]", id);
    }

}
