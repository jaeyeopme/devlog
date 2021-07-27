package io.devlog.devlog.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.common.email.EmailTokenService;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRegisterRequest;
import io.devlog.devlog.user.dto.UserResponse;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import io.devlog.devlog.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static io.devlog.devlog.user.exception.UserResponseStatusException.INVALID_TOKEN_EXCEPTION;
import static io.devlog.devlog.user.exception.UserResponseStatusException.USER_NOT_FOUND_EXCEPTION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private final Long userId = 1L;
    private final String email = "email@email.com";
    private final String emailToken = "emailToken";
    @MockBean
    UserService userService;
    @MockBean
    EmailTokenService emailTokenService;
    @MockBean
    PasswordEncoder passwordEncoder;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private UserRegisterRequest userRegisterRequest;
    private UserUpdateRequest userUpdateRequest;

    RequestPostProcessor createPrincipal(User user) {
        return SecurityMockMvcRequestPostProcessors.user(new PrincipalDetails(user));
    }

    String createUserRegisterRequestContent() throws JsonProcessingException {
        return objectMapper.writeValueAsString(userRegisterRequest);
    }

    String createUserUpdateRequestContent() throws JsonProcessingException {
        return objectMapper.writeValueAsString(userUpdateRequest);
    }

    String createUserResponseContent(User user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(UserResponse.from(user));
    }

    @BeforeEach
    void setUp() {
        userRegisterRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .nickname("updateNickname")
                .build();
    }

    @DisplayName("중복되지 않은 이메일로 회원가입한 경우 HTTP 상태코드 201을 반환한다.")
    @Test
    void registerNonDuplicatedEmail() throws Exception {
        given(userService.checkDuplicationEmail(any())).willReturn(false);

        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRegisterRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("중복된 이메일로 회원가입한 경우 HTTP 상태코드 409를 반환한다.")
    @Test
    void registerDuplicatedEmail() throws Exception {
        given(userService.checkDuplicationEmail(any())).willReturn(true);

        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRegisterRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict());
        // TODO: 21. 7. 27. Expect Content
    }

    @WithMockUser
    @DisplayName("회원가입 되어있는 사용자를 조회할 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void searchExistUser() throws Exception {
        User existUser = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        given(userService.findById(any())).willReturn(existUser);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/{id}", userId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(createUserResponseContent(existUser)));
    }

    @WithMockUser
    @DisplayName("회원가입 되어있지 않은 사용자를 조회할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void searchWithNotExistUser() throws Exception {
        given(userService.findById(any())).willThrow(USER_NOT_FOUND_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/{id}", userId);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("사용자를 찾을 수 없습니다."));
    }

    @DisplayName("사용자가 자신의 개인 프로필을 조회할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
    @Test
    void getProfile() throws Exception {
        User loginUser = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/my-profile")
                .with(createPrincipal(loginUser));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createUserResponseContent(loginUser)));
    }

    @DisplayName("사용자가 자신의 개인 프로필을 수정할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
    @Test
    void updateMyProfile() throws Exception {
        User loginUser = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        MockHttpServletRequestBuilder requestBuilder = put(USER_API_URI)
                .with(createPrincipal(loginUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserUpdateRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createUserResponseContent(loginUser)));
    }

    @DisplayName("사용자가 탈퇴할 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void deleteMyProfile() throws Exception {
        User loginUser = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        MockHttpServletRequestBuilder requestBuilder = delete(USER_API_URI)
                .with(createPrincipal(loginUser));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("중복된 이메일이 없을 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void duplicateCheckWithNonDuplicatedEmail() throws Exception {
        given(userService.checkDuplicationEmail(any())).willReturn(false);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/duplicate/{email}", email);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일이 중복되었을 경우 HTTP 상태코드 409와 메시지를 반환한다.")
    @Test
    void duplicateCheckWithDuplicatedEmail() throws Exception {
        given(userService.checkDuplicationEmail(any())).willReturn(true);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/duplicate/{email}", email);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("중복된 이메일 입니다."));
    }

    @DisplayName("이메일 토큰 전송에 성공한 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void sendEmailTokenToLoginUser() throws Exception {
        User loginUser = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI + "/email-verification")
                .with(createPrincipal(loginUser));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효한 이메일 토큰일 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void verifyValidEmailToken() throws Exception {
        User existUser = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        given(emailTokenService.verify(emailToken)).willReturn(email);
        given(userService.findByEmail(any())).willReturn(existUser);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 이메일 토큰일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void verifyInValidEmailToken() throws Exception {
        given(emailTokenService.verify(any())).willThrow(INVALID_TOKEN_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("인증토큰이 만료되었습니다."));
    }

    @DisplayName("유효한 이메일 토큰이지만 유효하지 않은 이메일일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void verifyValidEmailTokenAndInvalidEmail() throws Exception {
        given(emailTokenService.verify(any())).willReturn(email);
        given(userService.findByEmail(any())).willThrow(USER_NOT_FOUND_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("사용자를 찾을 수 없습니다."));
    }

}