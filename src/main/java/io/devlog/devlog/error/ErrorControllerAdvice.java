package io.devlog.devlog.error;

import io.devlog.devlog.error.exception.CommentAccessDeniedException;
import io.devlog.devlog.error.exception.CommentNotFoundException;
import io.devlog.devlog.error.exception.PostAccessDeniedException;
import io.devlog.devlog.error.exception.PostNotFoundException;
import io.devlog.devlog.error.exception.InvalidEmailTokenException;
import io.devlog.devlog.error.exception.UserDataDuplicationException;
import io.devlog.devlog.error.exception.UserEmailNotFoundException;
import io.devlog.devlog.error.exception.UserIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "이미 존재하는 사용자입니다.")
    @ExceptionHandler(UserDataDuplicationException.class)
    public void handleUserDataDuplication() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "사용자를 찾을 수 없습니다.")
    @ExceptionHandler({UserEmailNotFoundException.class, UserIdNotFoundException.class})
    public void handleUserNotFound() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "게시글을 찾을 수 없습니다.")
    @ExceptionHandler(PostNotFoundException.class)
    public void handlePostNotFound() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "댓글을 찾을 수 없습니다.")
    @ExceptionHandler(CommentNotFoundException.class)
    public void handleCommentNotFound() {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "유효하지 않은 이메일 토큰입니다.")
    @ExceptionHandler(InvalidEmailTokenException.class)
    public void handleInvalidEmailToken() {
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "접근 권한이 없습니다.")
    @ExceptionHandler({PostAccessDeniedException.class, CommentAccessDeniedException.class})
    public void handleAccessDenied() {
    }

}
