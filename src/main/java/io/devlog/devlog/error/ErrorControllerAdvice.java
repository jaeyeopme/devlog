package io.devlog.devlog.error;

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
    public ErrorResponse handleUserDataDuplication(UserDataDuplicationException e) {
        return new ErrorResponse(e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserEmailNotFoundException.class, UserIdNotFoundException.class})
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

}
