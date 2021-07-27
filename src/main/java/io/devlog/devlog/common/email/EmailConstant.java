package io.devlog.devlog.common.email;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailConstant {

    public static final String DOMAIN_NAME = "http://localhost:8080";
    public static final String EMAIL_CONFIRM_URL = "/api/users/verify-token";
    public static final String VERIFICATION_TITLE = "Devlog 인증번호 안내";

}