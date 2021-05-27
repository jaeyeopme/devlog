package io.devlog.devlog.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static io.devlog.devlog.docs.ApiDocumentationUtils.getDocumentRequest;
import static io.devlog.devlog.docs.ApiDocumentationUtils.getDocumentResponse;
import static io.devlog.devlog.fixture.MemberFixture.MEMBER_REGISTRATION_REQUEST;
import static io.devlog.devlog.fixture.MemberFixture.NEW_MEMBER;
import static io.devlog.devlog.member.controller.MemberController.MEMBER_API_URI;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

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
    @DisplayName("회원가입에 성공할 경우 HTTP 상태코드 201을 반환한다.")
    void registration_Success_201() throws Exception {
        doNothing().when(memberService).registration(NEW_MEMBER);

        this.mockMvc.perform(post(MEMBER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(MEMBER_REGISTRATION_REQUEST)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("members/create/success",
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
    @DisplayName("중복된 이메일이 존재할 경우 HTTP 상태코드 409와 에러메세지를 반환한다.")
    void memberRegistration_Fail_409_ErrorMessage() throws Exception {
        when(memberService.isDuplicatedEmail(NEW_MEMBER.getEmail())).thenReturn(true);

        this.mockMvc.perform(post(MEMBER_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(MEMBER_REGISTRATION_REQUEST)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(status().reason("중복된 이메일 입니다."))
                .andDo(document("members/create/fail",
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

}