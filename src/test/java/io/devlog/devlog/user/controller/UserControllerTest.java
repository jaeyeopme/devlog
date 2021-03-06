package io.devlog.devlog.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.common.email.EmailService;
import io.devlog.devlog.user.dto.UserRegisterRequest;
import io.devlog.devlog.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    PasswordEncoder passwordEncoder;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @DisplayName("중복되지 않은 이메일로 회원가입한 경우 HTTP 상태코드 201을 반환한다.")
    @Test
    void registerNonDuplicatedEmail() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("email@email.com")
                .nickname("nickname")
                .password("Password1234!@")
                .build();

        given(userService.isDuplicated(any())).willReturn(false);

        mockMvc.perform(post(USER_API_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

//    @DisplayName("중복된 이메일로 회원가입한 경우 HTTP 상태코드 409를 반환한다.")
//    @Test
//    void registerDuplicatedEmail() throws Exception {
//        UserRegisterRequest request = UserRegisterRequest.builder()
//                .email("duplicateEmail@email.com")
//                .nickname("nickname")
//                .password("Password1234!@")
//                .build();
//
//        given(userService.isDuplicated(any())).willReturn(true);
//
//        mockMvc.perform(post(USER_API_URI)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isConflict())
//                .andExpect(content().json(createErrorResponseContent("이미 존재하는 사용자입니다.")));
//    }
//
//    @WithMockUser
//    @DisplayName("회원가입 되어있는 사용자를 조회할 경우 HTTP 상태코드 200을 반환한다.")
//    @Test
//    void searchExistUser() throws Exception {
//        User existUser = User.builder()
//                .email("email@email.com")
//                .password("Password1234!")
//                .nickname("nickname")
//                .build();
//
//        given(userService.findById(any())).willReturn(existUser);
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/{id}", userId);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().json(createUserResponseContent(existUser)));
//    }
//
//    @WithMockUser
//    @DisplayName("회원가입 되어있지 않은 사용자를 조회할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
//    @Test
//    void searchWithNotExistUser() throws Exception {
//        given(userService.findById(any())).willThrow(new UserIdNotFoundException(any()));
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/{id}", userId);
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(content().json(createErrorResponseContent("사용자를 찾을 수 없습니다.")));
//    }
//
//    @DisplayName("사용자가 자신의 개인 프로필을 조회할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
//    @Test
//    void getProfile() throws Exception {
//        User loginUser = User.builder()
//                .email("email@email.com")
//                .password("Password1234!")
//                .nickname("nickname")
//                .build();
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/my-profile")
//                .with(principal(loginUser));
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(createUserResponseContent(loginUser)));
//    }
//
//    @DisplayName("사용자가 자신의 개인 프로필을 수정할 경우 HTTP 상태코드 200과 UserResponse 를 반환한다.")
//    @Test
//    void updateMyProfile() throws Exception {
//        User loginUser = User.builder()
//                .email("email@email.com")
//                .password("Password1234!")
//                .nickname("nickname")
//                .build();
//
//        MockHttpServletRequestBuilder requestBuilder = put(USER_API_URI)
//                .with(principal(loginUser))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(createUserUpdateRequestContent());
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(createUserResponseContent(loginUser)));
//    }
//
//    @DisplayName("사용자가 탈퇴할 경우 HTTP 상태코드 200을 반환한다.")
//    @Test
//    void deleteMyProfile() throws Exception {
//        User loginUser = User.builder()
//                .email("email@email.com")
//                .password("Password1234!")
//                .nickname("nickname")
//                .build();
//
//        MockHttpServletRequestBuilder requestBuilder = delete(USER_API_URI)
//                .with(principal(loginUser));
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("중복된 이메일이 없을 경우 HTTP 상태코드 200을 반환한다.")
//    @Test
//    void duplicateCheckWithNonDuplicatedEmail() throws Exception {
//        given(userService.isDuplicated(any())).willReturn(false);
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/duplicate/{email}", email);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("이메일이 중복되었을 경우 HTTP 상태코드 409와 메시지를 반환한다.")
//    @Test
//    void duplicateCheckWithDuplicatedEmail() throws Exception {
//        given(userService.isDuplicated(any())).willReturn(true);
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/duplicate/{email}", email);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isConflict())
//                .andExpect(content().json(createErrorResponseContent("이미 존재하는 사용자입니다.")));
//    }
//
//    @DisplayName("이메일 토큰 전송에 성공한 경우 HTTP 상태코드 200을 반환한다.")
//    @Test
//    void sendEmailTokenToLoginUser() throws Exception {
//        User loginUser = User.builder()
//                .email("email@email.com")
//                .password("Password1234!")
//                .nickname("nickname")
//                .build();
//
//        MockHttpServletRequestBuilder requestBuilder = post(USER_API_URI + "/email-verification")
//                .with(principal(loginUser));
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("유효한 이메일 토큰일 경우 HTTP 상태코드 200을 반환한다.")
//    @Test
//    void verifyValidEmailToken() throws Exception {
//        User existUser = User.builder()
//                .email("email@email.com")
//                .password("Password1234!")
//                .nickname("nickname")
//                .build();
//
//        given(emailService.getEmail(emailToken)).willReturn(email);
//        given(userService.findByEmail(any())).willReturn(existUser);
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("유효하지 않은 이메일 토큰일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
//    @Test
//    void verifyInValidEmailToken() throws Exception {
//        given(emailService.getEmail(any())).willThrow(new InvalidEmailTokenException(any()));
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().json(createErrorResponseContent("유효하지 않은 이메일 토큰입니다.")));
//    }
//
//    @DisplayName("유효한 이메일 토큰이지만 유효하지 않은 이메일일 경우 HTTP 상태코드 401과 메시지를 반환한다.")
//    @Test
//    void verifyValidEmailTokenAndInvalidEmail() throws Exception {
//        given(emailService.isInvalid(any())).willReturn(true);
//
//        MockHttpServletRequestBuilder requestBuilder = get(USER_API_URI + "/verify-token/{token}", emailToken);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().json(createErrorResponseContent("유효하지 않은 이메일 토큰입니다.")));
//    }

}