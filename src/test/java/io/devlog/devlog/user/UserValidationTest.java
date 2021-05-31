package io.devlog.devlog.user;

import io.devlog.devlog.user.dto.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("유효하지 않은 이메일 형식일 경우 유효성 검사에 실패한다.")
    @Test
    void isNotValidEmail() {
        UserRequest userRequest = UserRequest.builder()
                .email("member@email/com")
                .password("Member1234!")
                .nickname("member")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("유효하지 않은 이메일 형식입니다.");
    }

    @DisplayName("유효하지 않은 비밀번호 형식일 경우 유효성 검사에 실패한다.")
    @Test
    void isNotValidPassword() {
        UserRequest userRequest = UserRequest.builder()
                .email("member@email.com")
                .password("member1234!")
                .nickname("member")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

}
