package io.devlog.devlog.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CommentResponseException {

    public static ResponseStatusException COMMENT_NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");

}
