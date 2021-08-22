package io.devlog.devlog.user.service;

import io.devlog.devlog.error.exception.UserEmailNotFoundException;
import io.devlog.devlog.error.exception.UserIdNotFoundException;
import io.devlog.devlog.fixture.UserFixture;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GeneralUserServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    GeneralUserService userService;

    @DisplayName("회원가입에 성공한다면 사용자는 USER 권한을 가지고 있는다.")
    @Test
    void register() {
        assertTrue(UserFixture.USER.getAuthorities().isEmpty());

        userService.register(UserFixture.USER);

        assertFalse(UserFixture.USER.getAuthorities().isEmpty());
        then(userRepository).should(only()).save(any());
    }

    @DisplayName("존재하는 사용자의 ID로 사용자를 조회하는 경우 조회된 사용자를 반환한다.")
    @Test
    void findUserByExistUserId() {
        given(userRepository.findById(any())).willReturn(Optional.of(UserFixture.USER));

        User expectedUser = userService.findById(any());

        assertThat(UserFixture.USER).isEqualTo(expectedUser);
        then(userRepository).should(only()).findById(any());
    }

    @DisplayName("존재하지 않은 사용자의 ID로 사용자를 조회하는 경우 UserIdNotFoundException 이 발생한다.")
    @Test
    void findUserByNonExistUserId() {
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(UserIdNotFoundException.class,
                () -> userService.findById(UserFixture.ID), String.format("User id not found: [\"%s\"]", UserFixture.ID));

        then(userRepository).should(only()).findById(any());
    }

    @DisplayName("사용자의 프로필을 수정한다.")
    @Test
    void updateProfile() {
        given(userRepository.findByEmail(any())).willReturn(Optional.of(UserFixture.USER));

        User updatedProfile = userService.updateProfile(UserFixture.EMAIL, UserFixture.UPDATE_REQUEST);

        assertThat(updatedProfile.getNickname()).isEqualTo(UserFixture.UPDATE_NICKNAME);
    }

    @DisplayName("사용자의 프로필을 삭제한다.")
    @Test
    void deleteProfile() {
        willDoNothing().given(userRepository).deleteByEmail(any());

        userService.deleteProfileByEmail(any());

        then(userRepository).should(only()).deleteByEmail(any());
    }

    @DisplayName("존재하는 이메일로 중복검사를 하는 경우 실패한다.")
    @Test
    void duplicateCheckWithDuplicatedEmail() {
        given(userRepository.existsByEmail(any())).willReturn(true);

        boolean duplicated = userService.isDuplicated(any());

        assertTrue(duplicated);
        then(userRepository).should(only()).existsByEmail(any());
    }

    @DisplayName("존재하지 않는 이메일로 중복검사를 하는 경우 성공한다.")
    @Test
    void duplicateCheckWithNonDuplicatedEmail() {
        given(userRepository.existsByEmail(any())).willReturn(false);

        boolean duplicated = userService.isDuplicated(any());

        assertFalse(duplicated);
        then(userRepository).should(only()).existsByEmail(any());
    }

    @DisplayName("존재하는 사용자의 이메일로 사용자를 조회하는 경우 조회된 사용자를 반환한다.")
    @Test
    void findUserWithExistEmail() {
        given(userRepository.findByEmail(any())).willReturn(Optional.of(UserFixture.USER));

        User expectedUser = userService.findByEmail(any());

        assertThat(UserFixture.USER).isEqualTo(expectedUser);
        then(userRepository).should(only()).findByEmail(any());
    }

    @DisplayName("존재하지 않는 사용자의 이메일로 사용자를 조회하는 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void findUserByNonExistEmail() {
        given(userRepository.findByEmail(UserFixture.EMAIL)).willReturn(Optional.empty());

        assertThrows(UserEmailNotFoundException.class,
                () -> userService.findByEmail(UserFixture.EMAIL), String.format("User email not found: [\"%s\"]", UserFixture.EMAIL));

        then(userRepository).should(only()).findByEmail(any());
    }

    @DisplayName("사용자가 활성화된다.")
    @Test
    void setEnabled() {
        given(userRepository.findByEmail(any())).willReturn(Optional.of(UserFixture.USER));
        assertFalse(UserFixture.USER.isEnabled());

        userService.setEnable(UserFixture.EMAIL);

        assertTrue(UserFixture.USER.isEnabled());
        then(userRepository).should(only()).findByEmail(any());
    }

}