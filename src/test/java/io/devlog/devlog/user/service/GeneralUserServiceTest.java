package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.devlog.devlog.fixture.UserFixture.USER_REGISTRATION_REQUEST;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralUserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private GeneralUserService generalUserService;

    @DisplayName("중복된 이메일이 없을 경우 FALSE 를 반환한다.")
    @Test
    void isDuplicatedEmail_Duplicated_False() {
        when(userRepository.existsByEmail(any())).thenReturn(false);

        assertFalse(generalUserService.isDuplicatedEmail(USER_REGISTRATION_REQUEST.getEmail()));
    }

    @DisplayName("중복된 이메일이 있을 경우 TRUE 를 반환한다.")
    @Test
    void isDuplicatedEmail_NotDuplicated_True() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertTrue(generalUserService.isDuplicatedEmail(USER_REGISTRATION_REQUEST.getEmail()));
    }


}