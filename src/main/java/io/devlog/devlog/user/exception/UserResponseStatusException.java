package io.devlog.devlog.user.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseStatusException {

    public static ResponseStatusException INVALID_TOKEN_EXCEPTION = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증토큰이 만료되었습니다.");

}
