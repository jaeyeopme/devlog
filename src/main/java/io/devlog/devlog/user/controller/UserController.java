package io.devlog.devlog.user.controller;

import io.devlog.devlog.utils.EmailUtils;
import io.devlog.devlog.error.exception.InvalidEmailTokenException;
import io.devlog.devlog.error.exception.UserDataDuplicationException;
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
    private final EmailUtils emailUtils;
    private final PasswordEncoder passwordEncoder;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        String email = request.getEmail();

        if (userService.isDuplicated(email))
            throw new UserDataDuplicationException();

        userService.register(User.from(request, passwordEncoder));
        emailUtils.sendToken(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserResponse search(@PathVariable Long id) {
        return UserResponse.from(userService.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my-profile")
    public UserResponse findMyProfile(@AuthenticationPrincipal PrincipalDetails details) {
        String email = details.getEmail();

        return UserResponse.from(userService.findByEmail(email));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public UserResponse updateMyProfile(@Valid @RequestBody UserUpdateRequest request,
                                        @AuthenticationPrincipal PrincipalDetails details) {
        String email = details.getEmail();

        return UserResponse.from(userService.updateProfile(email, request));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/duplicate/{email}")
    public void checkDuplication(@PathVariable String email) {
        if (userService.isDuplicated(email))
            throw new UserDataDuplicationException();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteMyProfile(@AuthenticationPrincipal PrincipalDetails details) {
        userService.deleteProfileByEmail(details.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/email-verification")
    public void sendEmailToken(@AuthenticationPrincipal PrincipalDetails details) {
        emailUtils.sendToken(details.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/verify-token/{token}")
    public void verifyEmailToken(@PathVariable String token) {
        String email = emailUtils.getEmail(token);

        if (emailUtils.isInvalid(email))
            throw new InvalidEmailTokenException(email);

        userService.setEnable(email);
    }

}
