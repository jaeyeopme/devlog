package io.devlog.devlog.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.common.email.EmailService;
import io.devlog.devlog.common.security.WithMockPrincipal;
import io.devlog.devlog.error.user.InvalidEmailTokenException;
import io.devlog.devlog.error.user.UserIdNotFoundException;
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
import org.springframework.test.web.servlet.MockMvc;

import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    EmailService emailService;
    @MockBean
    PasswordEncoder encoder;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    UserRegisterRequest registerRequest;
    UserUpdateRequest updateRequest;
    UserResponse response;
    User user;

    @BeforeEach
    void setUp() {
        registerRequest = UserRegisterRequest.builder()
                .email("email@email.com")
                .nickname("nickname")
                .password("Password1234!")
                .build();

        user = User.from(registerRequest, encoder);

        response = UserResponse.from(user);

        updateRequest = UserUpdateRequest.builder()
                .nickname("nickname")
                .build();
    }

    private String toJSON(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    @DisplayName("중복되지 않은 이메일로 회원가입한 경우 HTTP 상태코드 201을 반환한다.")
    @Test
    void registerNonDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(anyString())).willReturn(false);

        mockMvc.perform(post(USER_API_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(registerRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("중복된 이메일로 회원가입한 경우 HTTP 상태코드 409와 메시지를 반환한다.")
    @Test
    void registerDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(anyString())).willReturn(true);

        mockMvc.perform(post(USER_API_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(registerRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("이미 존재하는 사용자입니다."));
    }

    @WithMockPrincipal
    @DisplayName("회원가입 되어있는 사용자를 조회할 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void searchExistUser() throws Exception {
        given(userService.findById(anyLong())).willReturn(user);

        mockMvc.perform(get(USER_API_URI + "/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(toJSON(response)));
    }

    @WithMockPrincipal
    @DisplayName("회원가입 되어있지 않은 사용자를 조회할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void searchWithNotExistUser() throws Exception {
        given(userService.findById(anyLong())).willThrow(new UserIdNotFoundException(1L));

        mockMvc.perform(get(USER_API_URI + "/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("사용자를 찾을 수 없습니다."));
    }

    @WithMockPrincipal
    @DisplayName("사용자가 자신의 개인 프로필을 조회할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
    @Test
    void getProfile() throws Exception {
        given(userService.findByEmail(anyString())).willReturn(user);

        mockMvc.perform(get(USER_API_URI + "/my-profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(toJSON(response)));
    }

    @WithMockPrincipal
    @DisplayName("사용자가 자신의 개인 프로필을 수정할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
    @Test
    void updateMyProfile() throws Exception {
        given(userService.updateProfile(anyString(), any(UserUpdateRequest.class))).willReturn(user);

        mockMvc.perform(put(USER_API_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(toJSON(response)));
    }

    @WithMockPrincipal
    @DisplayName("사용자가 탈퇴할 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void deleteMyProfile() throws Exception {
        mockMvc.perform(delete(USER_API_URI))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("중복된 이메일이 없을 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void duplicateCheckWithNonDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(any())).willReturn(false);

        mockMvc.perform(get(USER_API_URI + "/duplicate/{email}", "email@email.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일이 중복되었을 경우 HTTP 상태코드 409와 메시지를 반환한다.")
    @Test
    void duplicateCheckWithDuplicatedEmail() throws Exception {
        given(userService.isDuplicated(any())).willReturn(true);

        mockMvc.perform(get(USER_API_URI + "/duplicate/{email}", "email@email.com"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("이미 존재하는 사용자입니다."));
    }

    @WithMockPrincipal
    @DisplayName("이메일 토큰 전송에 성공한 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void sendEmailTokenToLoginUser() throws Exception {
        mockMvc.perform(post(USER_API_URI + "/email-verification"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효한 이메일 토큰일 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void verifyValidEmailToken() throws Exception {
        given(emailService.getEmail(anyString())).willReturn("email@email.com");
        given(emailService.isInvalid(anyString())).willReturn(false);

        mockMvc.perform(get(USER_API_URI + "/verify-token/{token}", "emailToken"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 이메일 토큰일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void verifyInValidEmailToken() throws Exception {
        given(emailService.getEmail(anyString())).willThrow(new InvalidEmailTokenException("emailToken"));

        mockMvc.perform(get(USER_API_URI + "/verify-token/{token}", "emailToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("유효하지 않은 이메일 토큰입니다."));
    }

    @DisplayName("유효한 이메일 토큰이지만 유효하지 않은 이메일일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void verifyValidEmailTokenAndInvalidEmail() throws Exception {
        given(emailService.isInvalid(any())).willReturn(true);

        mockMvc.perform(get(USER_API_URI + "/verify-token/{token}", "emailToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("유효하지 않은 이메일 토큰입니다."));
    }

}