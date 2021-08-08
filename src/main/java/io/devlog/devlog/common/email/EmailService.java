package io.devlog.devlog.common.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private static final String VERIFICATION_TITLE = "Devlog 인증번호 안내";
    private static final String DOMAIN_NAME = "http://localhost:8080";
    private static final String EMAIL_CONFIRM_URL = "/api/users/verify-token";
    private static final long EMAIL_TOKEN_EXPIRE_TIME = 180L;

    private final RedisService redisService;
    private final JavaMailSender javaMailSender;

    public void sendToken(String email) {
        String token = generateEmailToken(email);

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

    public String getEmail(String token) {
        return redisService.get(token);
    }

    public boolean isInvalid(String email) {
        return !email.isBlank();
    }

    private String buildContent(String token) {
        return DOMAIN_NAME +
                EMAIL_CONFIRM_URL +
                "/" +
                token;
    }

    private String generateEmailToken(String email) {
        String token = String.valueOf(UUID.randomUUID());
        redisService.set(token, email, EMAIL_TOKEN_EXPIRE_TIME);

        return token;
    }

    private void deleteToken(String token) {
        redisService.deleteToken(token);
    }

}
