package io.devlog.devlog.common.service;

import io.devlog.devlog.common.util.EmailTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.devlog.devlog.common.constant.EmailConstant.*;
import static io.devlog.devlog.user.exception.UserResponseStatusException.INVALID_TOKEN_EXCEPTION;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailTokenService emailTokenService;
    private final JavaMailSender javaMailSender;

    public void sendEmailToken(String email) {
        String token = generateToken();

        saveToken(token, email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(DOMAIN_NAME);
        message.setText(buildContent(token));
        message.setSubject(VERIFICATION_TITLE);
        message.setTo(email);

        javaMailSender.send(message);
    }

    public String buildContent(String token) {
        return new StringBuilder()
                .append(DOMAIN_NAME)
                .append(EMAIL_CONFIRM_URL)
                .append("?token=")
                .append(token)
                .toString();
    }

    public String generateToken() {
        return String.valueOf(UUID.randomUUID());
    }

    public void saveToken(String token, String email) {
        emailTokenService.setToken(token, email, VERIFICATION_TIMEOUT);
    }

    public void deleteToken(String token) {
        emailTokenService.deleteToken(token);
    }

    public boolean isInvalidToken(String token) {
        return emailTokenService.hasToken(token);
    }

    public String getVerifiedEmail(String token) {
        String email = emailTokenService.getEmail(token).orElseThrow(() -> INVALID_TOKEN_EXCEPTION);
        deleteToken(token);
        return email;
    }

}
