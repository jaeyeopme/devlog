package io.devlog.devlog.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PostResponseStatusException {

    public static ResponseStatusException POST_NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");


}
