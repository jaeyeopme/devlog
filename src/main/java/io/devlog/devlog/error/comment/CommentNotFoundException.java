package io.devlog.devlog.error.comment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long id) {
        log.error("Comment id not found: [{}]", id);
    }

}
