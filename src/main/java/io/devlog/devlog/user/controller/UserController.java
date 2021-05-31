package io.devlog.devlog.user.controller;

import io.devlog.devlog.common.service.EmailVerificationService;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRequest;
import io.devlog.devlog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.common.constant.ResponseEntityConstant.RESPONSE_CREATED;
import static io.devlog.devlog.common.constant.ResponseEntityConstant.RESPONSE_OK;
import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static io.devlog.devlog.user.exception.UserResponseStatusException.DUPLICATED_EMAIL_EXCEPTION;
import static io.devlog.devlog.user.exception.UserResponseStatusException.INVALID_TOKEN_EXCEPTION;

@RequestMapping(USER_API_URI)
@RequiredArgsConstructor
@RestController
public class UserController {

    public static final String USER_API_URI = "/api/users";
    private final UserService userService;
    private final EmailVerificationService verificationService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<HttpStatus> registration(@Valid @RequestBody UserRequest userRequest) {
        if (userService.isDuplicatedEmail(userRequest.getEmail()))
            throw DUPLICATED_EMAIL_EXCEPTION;

        userService.register(UserRequest.toEntity(userRequest, passwordEncoder));

        return RESPONSE_CREATED;
    }

    @PostMapping("/duplicated/{email}")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable String email) {
        if (userService.isDuplicatedEmail(email))
            throw DUPLICATED_EMAIL_EXCEPTION;

        return RESPONSE_OK;
    }

    @PostMapping("/email-verification")
    public ResponseEntity<HttpStatus> sendVerificationEmail(@AuthenticationPrincipal User user) {
        verificationService.sendEmail(user.getEmail());

        return RESPONSE_OK;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<HttpStatus> verifyEmail(@RequestParam String token) {
        if (verificationService.isInvalidToken(token))
            throw INVALID_TOKEN_EXCEPTION;

        userService.setEnabled(verificationService.getVerifiedEmail(token));

        return RESPONSE_OK;
    }

}
