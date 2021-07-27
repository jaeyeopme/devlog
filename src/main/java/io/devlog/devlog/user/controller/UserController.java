package io.devlog.devlog.user.controller;

import io.devlog.devlog.common.email.EmailTokenService;
import io.devlog.devlog.error.user.UserDataDuplicationException;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRegisterRequest;
import io.devlog.devlog.user.dto.UserResponse;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import io.devlog.devlog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.common.HttpStatusResponseEntity.RESPONSE_CREATED;
import static io.devlog.devlog.common.HttpStatusResponseEntity.RESPONSE_OK;
import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static io.devlog.devlog.user.exception.UserResponseStatusException.DUPLICATED_EMAIL_EXCEPTION;

@RequiredArgsConstructor
@RequestMapping(USER_API_URI)
@RestController
public class UserController {

    public static final String USER_API_URI = "/api/users";
    private final UserService userService;
    private final EmailTokenService emailTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@Valid @RequestBody UserRegisterRequest request) {
        if (userService.checkDuplicationEmail(request.getEmail()))
            throw new UserDataDuplicationException();

        userService.register(User.from(request, passwordEncoder));
        emailTokenService.sendEmailToken(request.getEmail());

        return RESPONSE_CREATED;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> search(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.from(userService.findById(id)));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal PrincipalDetails userDetails) {
        return ResponseEntity.ok(UserResponse.from(userDetails.getUser()));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateMyProfile(@Valid @RequestBody UserUpdateRequest request,
                                                        @AuthenticationPrincipal PrincipalDetails userDetails) {
        userService.updateProfile(userDetails.getUser(), request);
        return ResponseEntity.ok(UserResponse.from(userDetails.getUser()));
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteMyProfile(@AuthenticationPrincipal PrincipalDetails userDetails) {
        userService.deleteProfile(userDetails.getUser().getId());
        return RESPONSE_OK;
    }

    @GetMapping("/duplicate/{email}")
    public ResponseEntity<HttpStatus> checkDuplication(@PathVariable String email) {
        if (userService.checkDuplicationEmail(email))
            throw new UserDataDuplicationException();

        return RESPONSE_OK;
    }

    @PostMapping("/email-verification")
    public ResponseEntity<HttpStatus> sendEmailToken(@AuthenticationPrincipal PrincipalDetails userDetails) {
        emailTokenService.sendEmailToken(userDetails.getUser().getEmail());

        return RESPONSE_OK;
    }

    @GetMapping("/verify-token/{token}")
    public ResponseEntity<HttpStatus> verifyEmailToken(@PathVariable String token) {
        String email = emailTokenService.verify(token);

        userService.setEnabled(userService.findByEmail(email));

        return RESPONSE_OK;
    }

}
