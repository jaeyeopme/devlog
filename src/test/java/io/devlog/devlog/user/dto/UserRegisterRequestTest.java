package io.devlog.devlog.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("유효한 이메일 형식일 경우 유효성 검사에 성공한다.")
    @Test
    void validateWithValidEmail() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isZero();
    }

    @DisplayName("유효하지 않은 이메일 형식일 경우 유효성 검사에 실패한다.")
    @Test
    void validateWithInvalidEmail() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email/com")
                .password("Password1234!")
                .nickname("email")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("유효하지 않은 이메일 형식입니다.");
    }

    @DisplayName("대문자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutUppercase() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("password1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("소문자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutLowerCase() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("PASSWORD1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("숫자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutNumber() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("Password!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("특수문자가 없는 비밀번호일 경우 유효성 검사에 실패한다.")
    @Test
    void validatePasswordWithoutSpecialCharacters() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("Password1234")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.");
    }

    @DisplayName("공백이 아닌 닉네임일 경우 유효성 검사에 성공한다.")
    @Test
    void validateWithNotBlankNickname() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isZero();
    }

    @DisplayName("공백인 닉네임일 경우 유효성 검사에 실패한다.")
    @Test
    void validateWithBlankNickname() {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("")
                .build();

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(userRegisterRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("닉네임을 입력해주세요.");
    }

}