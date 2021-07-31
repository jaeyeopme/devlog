package io.devlog.devlog.user.service;

import io.devlog.devlog.error.user.UserEmailNotFoundException;
import io.devlog.devlog.error.user.UserIdNotFoundException;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
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

    private UserUpdateRequest userUpdateRequest;
    private User user;

    @BeforeEach
    void setUp() {
        userUpdateRequest = UserUpdateRequest.builder()
                .nickname("updateNickname")
                .build();

        user = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();
    }

    @DisplayName("회원가입에 성공한다.")
    @Test
    void register() {
        assertTrue(user.getAuthorities().isEmpty());

        userService.register(user);

        assertFalse(user.getAuthorities().isEmpty());
        then(userRepository).should(only()).save(any());
    }

    @DisplayName("존재하는 사용자의 ID로 사용자를 조회하는 경우 조회된 사용자를 반환한다.")
    @Test
    void findUserByExistUserId() {
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        User findUser = userService.findById(any());

        assertThat(user).isEqualTo(findUser);
        then(userRepository).should(only()).findById(any());
    }

    @DisplayName("존재하지 않은 사용자의 ID로 사용자를 조회하는 경우 실패한다.")
    @Test
    void findUserByNonExistUserId() {
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(UserIdNotFoundException.class, () -> userService.findById(any()));

        then(userRepository).should(only()).findById(any());
    }

    @DisplayName("사용자의 프로필을 수정한다.")
    @Test
    void updateProfile() {
        assertThat(user.getNickname()).isNotEqualTo(userUpdateRequest.getNickname());

        userService.updateProfile(user, userUpdateRequest);

        assertThat(user.getNickname()).isEqualTo(userUpdateRequest.getNickname());
    }

    @DisplayName("사용자의 프로필을 삭제한다.")
    @Test
    void deleteProfile() {
        willDoNothing().given(userRepository).deleteById(any());

        userService.deleteProfile(any());

        then(userRepository).should(only()).deleteById(any());
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
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));

        User findUser = userService.findBy(any());

        assertThat(user).isEqualTo(findUser);
        then(userRepository).should(only()).findByEmail(any());
    }

    @DisplayName("존재하지 않는 사용자의 이메일로 사용자를 조회하는 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void findUserByNonExistEmail() {
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());

        assertThrows(UserEmailNotFoundException.class, () -> userService.findBy(any()));

        then(userRepository).should(only()).findByEmail(any());
    }

    @DisplayName("사용자가 활성화된다.")
    @Test
    void setEnabled() {
        assertFalse(user.isEnabled());

        userService.setEnabled(user);

        assertTrue(user.isEnabled());
    }

}