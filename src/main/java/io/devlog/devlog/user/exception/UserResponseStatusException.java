package io.devlog.devlog.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserResponseStatusException {

    public static ResponseStatusException DUPLICATED_EMAIL_EXCEPTION = new ResponseStatusException(HttpStatus.CONFLICT, "중복된 이메일 입니다.");
    public static ResponseStatusException INVALID_TOKEN_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증토큰이 만료되었습니다.");
    public static ResponseStatusException USER_NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");


}
