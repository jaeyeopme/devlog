package io.devlog.devlog.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MemberResponseStatusException {

    public static ResponseStatusException DUPLICATED_EMAIL_EXCEPTION = new ResponseStatusException(HttpStatus.CONFLICT, "중복된 이메일 입니다.");

}
