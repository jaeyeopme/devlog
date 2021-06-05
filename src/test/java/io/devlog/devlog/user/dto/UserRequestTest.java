package io.devlog.devlog.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("유효한 이메일 형식일 경우 유효성 검사에 성공한다.")
    @Test
    void validateWithValidEmail() {
        UserRequest validUserRequest = UserRequest.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(validUserRequest);

        assertThat(violations.size()).isZero();
    }

    @DisplayName("유효하지 않은 이메일 형식일 경우 유효성 검사에 실패한다.")
    @Test
    void validateWithInvalidEmail() {
        UserRequest notValidEmailRequest = UserRequest.builder()
                .email("email@email/com")
                .password("Password1234!")
                .nickname("email")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(notValidEmailRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("유효하지 않은 이메일 형식입니다.");
    }

    @DisplayName("대문자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutUppercase() {
        UserRequest invalidRequest = UserRequest.builder()
                .email("email@email.com")
                .password("password1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("소문자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutLowerCase() {
        UserRequest invalidRequest = UserRequest.builder()
                .email("email@email.com")
                .password("PASSWORD1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("숫자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutNumber() {
        UserRequest invalidRequest = UserRequest.builder()
                .email("email@email.com")
                .password("Password!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("특수문자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutSpecialCharacters() {
        UserRequest invalidRequest = UserRequest.builder()
                .email("email@email.com")
                .password("Password1234")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

}