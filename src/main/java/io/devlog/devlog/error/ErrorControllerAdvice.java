package io.devlog.devlog.error;

import io.devlog.devlog.error.comment.CommentAccessDeniedException;
import io.devlog.devlog.error.comment.CommentNotFoundException;
import io.devlog.devlog.error.post.PostAccessDeniedException;
import io.devlog.devlog.error.post.PostNotFoundException;
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

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "이미 존재하는 사용자입니다.")
    @ExceptionHandler(UserDataDuplicationException.class)
    public void handleUserDataDuplication() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "사용자를 찾을 수 없습니다.")
    @ExceptionHandler({UserEmailNotFoundException.class, UserIdNotFoundException.class})
    public void handleUserNotFound() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    public ErrorResponse handlePostNotFound() {
        return new ErrorResponse("게시글을 찾을 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentNotFoundException.class)
    public ErrorResponse handleCommentNotFound() {
        return new ErrorResponse("댓글을 찾을 수 없습니다.");
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
