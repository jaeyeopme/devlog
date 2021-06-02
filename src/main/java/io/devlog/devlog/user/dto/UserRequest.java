package io.devlog.devlog.user.dto;

import io.devlog.devlog.user.domain.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * ObjectMapper 는 @RequestBody 가 Property 로 구현되어 있을때 Getter 나 Setter 로 필드명을 매칭하며 reflection 을 사용해서 값을 주입합니다.
 * Property 로 구현되지 않았거나 생성을 위임한 경우가 아니라면 기본 생성자로 생성됩니다.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    public static User toEntity(UserRequest userRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(userRequest.email)
                .password(passwordEncoder.encode(userRequest.password))
                .nickname(userRequest.nickname)
                .build();
    }

}
