package io.devlog.devlog.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.common.email.EmailTokenService;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRequest;
import io.devlog.devlog.user.dto.UserResponse;
import io.devlog.devlog.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static io.devlog.devlog.user.exception.UserResponseStatusException.INVALID_TOKEN_EXCEPTION;
import static io.devlog.devlog.user.exception.UserResponseStatusException.USER_NOT_FOUND_EXCEPTION;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private final Long userId = 1L;
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
    private User user;
    private RequestPostProcessor principal;
    private UserRequest userRequest;
    private String userRequestContent;
    private String userResponseContent;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        userRequest = UserRequest.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        user = UserRequest.toEntity(userRequest, passwordEncoder);

        principal = SecurityMockMvcRequestPostProcessors.user(new PrincipalDetails(user));

        userRequestContent = objectMapper.writeValueAsString(userRequest);

        userResponseContent = objectMapper.writeValueAsString(UserResponse.of(user));
    }

    @DisplayName("중복되지 않은 이메일로 회원가입한 경우 HTTP 상태코드 201을 반환한다.")
    @Test
    void registerWithNotDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(userRequest.getEmail())).willReturn(false);
        willDoNothing().given(userService).register(user);
        willDoNothing().given(emailTokenService).sendEmailToken(userRequest.getEmail());

        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestContent);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("중복된 이메일로 회원가입한 경우 HTTP 상태코드 409를 반환한다.")
    @Test
    void registerWithDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(userRequest.getEmail())).willReturn(true);

        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestContent);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("중복된 이메일 입니다."));
    }

    @DisplayName("회원가입 되어있는 사용자를 조회할 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void searchWithExistUser() throws Exception {
        given(userService.findById(userId)).willReturn(user);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/{id}", userId)
                .with(principal);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(userResponseContent));
    }

    @DisplayName("회원가입 되어있지 않은 사용자를 조회할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void searchWithNotExistUser() throws Exception {
        given(userService.findById(userId)).willThrow(USER_NOT_FOUND_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/{id}", userId)
                .with(principal);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("사용자를 찾을 수 없습니다."));
    }

    @DisplayName("접속되어있는 사용자가 개인 프로필을 조회할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
    @Test
    void getProfile() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/my-profile")
                .with(principal);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(userResponseContent));
    }

    @DisplayName("접속되어있는 사용자가 개인 프로필을 수정할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
    @Test
    void updateProfile() throws Exception {
        willDoNothing().given(userService).updateUserProfile(user, userRequest);

        MockHttpServletRequestBuilder requestBuilder = put(USER_API_URI)
                .with(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestContent);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(userResponseContent));
    }

    @DisplayName("중복된 이메일이 없을 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void validateWithNotDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(userRequest.getEmail())).willReturn(false);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/duplicate/{email}", userRequest.getEmail());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일이 중복되었을 경우 HTTP 상태코드 409와 메시지를 반환한다.")
    @Test
    void validateWithDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(userRequest.getEmail())).willReturn(true);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/duplicate/{email}", userRequest.getEmail());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("중복된 이메일 입니다."));
    }


    @DisplayName("이메일 토큰 전송에 성공한 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void sendEmailTokenWithLoginUser() throws Exception {
        willDoNothing().given(emailTokenService).sendEmailToken(userRequest.getEmail());

        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI + "/email-verification")
                .with(principal);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효한 이메일 토큰일 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void verifyWithValidEmailToken() throws Exception {
        given(emailTokenService.verify(emailToken)).willReturn(userRequest.getEmail());

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 이메일 토큰일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void verifyWithInvalidEmailToken() throws Exception {
        given(emailTokenService.verify(emailToken)).willThrow(INVALID_TOKEN_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("인증토큰이 만료되었습니다."));
    }

}