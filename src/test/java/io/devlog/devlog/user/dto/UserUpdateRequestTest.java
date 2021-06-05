package io.devlog.devlog.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserUpdateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("공백이 아닌 닉네임일 경우 유효성 검사에 성공한다.")
    @Test
    void validateWithNotBlankNickname() {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .nickname("nickname")
                .build();

        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(userUpdateRequest);

        assertThat(violations.size()).isZero();
    }

    @DisplayName("공백인 닉네임일 경우 유효성 검사에 실패한다.")
    @Test
    void validateWithBlankNickname() {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .nickname("")
                .build();

        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(userUpdateRequest);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("닉네임을 입력해주세요.");
    }
}