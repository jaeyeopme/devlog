package io.devlog.devlog.user.controller;

import io.devlog.devlog.common.email.EmailService;
import io.devlog.devlog.error.user.InvalidEmailTokenException;
import io.devlog.devlog.error.user.UserDataDuplicationException;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRegisterRequest;
import io.devlog.devlog.user.dto.UserResponse;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import io.devlog.devlog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.user.controller.UserController.USER_API_URI;

@RequiredArgsConstructor
@RequestMapping(USER_API_URI)
@RestController
public class UserController {

    public static final String USER_API_URI = "/api/users";
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        String email = request.getEmail();

        if (userService.isDuplicated(email))
            throw new UserDataDuplicationException();

        userService.register(User.from(request, passwordEncoder));
        emailService.sendToken(email);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse search(@PathVariable Long id) {
        return UserResponse.from(userService.findById(id));
    }

    @GetMapping("/my-profile")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse findMyProfile(@AuthenticationPrincipal PrincipalDetails details) {
        String email = details.getEmail();

        return UserResponse.from(userService.findByEmail(email));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateMyProfile(@Valid @RequestBody UserUpdateRequest request,
                                        @AuthenticationPrincipal PrincipalDetails details) {
        String email = details.getEmail();

        User user = userService.updateProfile(email, request);

        return UserResponse.from(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/duplicate/{email}")
    public void checkDuplication(@PathVariable String email) {
        if (userService.isDuplicated(email))
            throw new UserDataDuplicationException();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMyProfile(@AuthenticationPrincipal PrincipalDetails details) {
        userService.deleteProfileByEmail(details.getEmail());
    }

    @PostMapping("/email-verification")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmailToken(@AuthenticationPrincipal PrincipalDetails details) {
        emailService.sendToken(details.getEmail());
    }

    @GetMapping("/verify-token/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void verifyEmailToken(@PathVariable String token) {
        String email = emailService.getEmail(token);

        if (emailService.isInvalid(email))
            throw new InvalidEmailTokenException(email);

        userService.setEnable(email);
    }

}
