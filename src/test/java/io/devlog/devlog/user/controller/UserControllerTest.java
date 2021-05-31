package io.devlog.devlog.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.common.service.EmailVerificationService;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRequest;
import io.devlog.devlog.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static io.devlog.devlog.docs.ApiDocumentationUtil.getDocumentRequest;
import static io.devlog.devlog.docs.ApiDocumentationUtil.getDocumentResponse;
import static io.devlog.devlog.fixture.UserFixture.*;
import static io.devlog.devlog.user.controller.UserController.USER_API_URI;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    EmailVerificationService emailVerificationService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void registration_Success() throws Exception {
        User user = UserRequest.toEntity(USER_REQUEST, passwordEncoder);
        willDoNothing().given(userService).register(user);

        mockMvc.perform(post(USER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(USER_REQUEST)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("users/create/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("로그인시 사용할 사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("하나 이상의 대소문자, 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("사용자의 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입에 실패한다.")
    void registration_Fail() throws Exception {
        given(userService.isDuplicatedEmail(USER_REQUEST.getEmail())).willReturn(true);

        mockMvc.perform(post(USER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(USER_REQUEST)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("중복된 이메일 입니다."))
                .andDo(document("users/create/fail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("로그인시 사용할 사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("하나 이상의 대소문자, 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("사용자의 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 중복 검사 - 사용 가능한 이메일")
    void idDuplicatedEmail_Success() throws Exception {
        given(userService.isDuplicatedEmail(USER_REQUEST.getEmail())).willReturn(false);

        mockMvc.perform(RestDocumentationRequestBuilders.get(USER_API_URI + "/duplicated/{email}", USER_REQUEST.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("users/duplicateVerification/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("email").description("로그인시 사용할 사용자 이메일"))));
    }

    @Test
    @DisplayName("이메일 중복 검사 - 중복된 이메일")
    void idDuplicatedEmail_Fail() throws Exception {
        given(userService.isDuplicatedEmail(USER_REQUEST.getEmail())).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders.get(USER_API_URI + "/duplicated/{email}", USER_REQUEST.getEmail()))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("중복된 이메일 입니다."))
                .andDo(document("users/duplicateVerification/fail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("email").description("로그인시 사용할 사용자 이메일"))));
    }

    @Test
    @DisplayName("이메일 토큰 검증 - 유효한 토큰이라면 인증에 성공한다.")
    void verifyEmailToken_Success() throws Exception {
        given(emailVerificationService.isInvalidToken(EMAIL_TOKEN)).willReturn(false);

        mockMvc.perform(RestDocumentationRequestBuilders.get(USER_API_URI + "/verify-token/{token}", EMAIL_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("users/tokenVerification/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("token").description("이메일 인증에 사용할 토큰"))));
    }

    @Test
    @DisplayName("이메일 토큰 검증 - 유효하지 않은 토큰이라면 인증에 실패한다.")
    void verifyEmailToken_Fail() throws Exception {

        given(emailVerificationService.isInvalidToken(EMAIL_TOKEN)).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders.get(USER_API_URI + "/verify-token/{token}", EMAIL_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("인증토큰이 만료되었습니다."))
                .andDo(document("users/tokenVerification/fail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("token").description("이메일 인증에 사용할 토큰"))));
    }

}