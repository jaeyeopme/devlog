package io.devlog.devlog.error;

import io.devlog.devlog.error.common.CommentAccessDeniedException;
import io.devlog.devlog.error.post.PostAccessDeniedException;
import io.devlog.devlog.error.user.InvalidEmailTokenException;
import io.devlog.devlog.error.user.UserDataDuplicationException;
import io.devlog.devlog.error.user.UserEmailNotFoundException;
import io.devlog.devlog.error.user.UserIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserDataDuplicationException.class)
    public ErrorResponse handleUserDataDuplication() {
        return new ErrorResponse("이미 존재하는 사용자입니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserEmailNotFoundException.class, UserIdNotFoundException.class})
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("사용자를 찾을 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidEmailTokenException.class)
    public ErrorResponse handleInvalidEmailToken() {
        return new ErrorResponse("유효하지 않은 이메일 토큰입니다.");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({PostAccessDeniedException.class, CommentAccessDeniedException.class})
    public ErrorResponse handleAccessDenied() {
        return new ErrorResponse("접근 권한이 없습니다.");
    }

}
