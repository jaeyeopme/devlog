package io.devlog.devlog.common.token;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static io.devlog.devlog.common.constant.EmailConstant.*;
import static io.devlog.devlog.user.exception.UserResponseStatusException.INVALID_TOKEN_EXCEPTION;

@RequiredArgsConstructor
@Service
public class EmailTokenService {

    private final TokenRedis tokenRedis;
    private final JavaMailSender javaMailSender;

    // 인증 메일 전송
    public void sendEmailToken(String email) {
        String token = generateToken(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(buildContent(token));
        message.setSubject(VERIFICATION_TITLE);
        message.setTo(email);

        javaMailSender.send(message);
    }

    // 인증 메일 내용 생성
    public String buildContent(String token) {
        return new StringBuilder()
                .append(DOMAIN_NAME)
                .append(EMAIL_CONFIRM_URL)
                .append("/" + token)
                .toString();
    }

    // 인증 토큰 생성 및 저장
    public String generateToken(String email) {
        return tokenRedis.generateToken(email, TOKEN_EXPIRED_TIME);
    }

    // 인증 토큰 삭제 후 이메일을 가져옴.
    public String verify(String token) {
        return tokenRedis.getEmail(token)
                .orElseThrow(() -> INVALID_TOKEN_EXCEPTION);
    }

}
