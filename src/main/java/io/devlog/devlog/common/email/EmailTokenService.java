package io.devlog.devlog.common.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

import static io.devlog.devlog.common.email.EmailConstant.*;
import static io.devlog.devlog.common.redis.RedisConfigConstant.EMAIL_TOKEN_EXPIRE_TIME;
import static io.devlog.devlog.user.exception.UserResponseStatusException.INVALID_TOKEN_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailTokenService {

    private final TokenRedisService tokenRedisService;
    private final JavaMailSender javaMailSender;

    // 인증 메일 전송
    public void sendEmailToken(String email) {
        String token = generateToken(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(buildContent(token));
        message.setSubject(VERIFICATION_TITLE);
        message.setTo(email);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            deleteToken(token);
            log.warn("Failed to send email to: '{}' when: '{}' reason: {}", email, new Date(), e.toString());
        }
    }

    // 인증 메일 내용 생성
    public String buildContent(String token) {
        return DOMAIN_NAME +
                EMAIL_CONFIRM_URL +
                "/" + token;
    }

    // 인증 토큰 생성 및 저장
    public String generateToken(String email) {
        return tokenRedisService.generateToken(email, EMAIL_TOKEN_EXPIRE_TIME);
    }

    // 인증 토큰 삭제 후 이메일을 가져옴.
    public String verify(String token) {
        String email = tokenRedisService.getEmail(token)
                .orElseThrow(() -> INVALID_TOKEN_EXCEPTION);
        deleteToken(token);

        return email;
    }

    public void deleteToken(String token) {
        tokenRedisService.deleteToken(token);
    }


}
