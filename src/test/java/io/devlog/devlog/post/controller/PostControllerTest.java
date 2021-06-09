package io.devlog.devlog.post.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.post.dto.PostResponse;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import io.devlog.devlog.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static io.devlog.devlog.post.controller.PostController.POST_API_URI;
import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_NOT_FOUND_EXCEPTION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    private final Long postId = 1L;
    @MockBean
    private PostService postService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private PostRequest postRequest;
    private User user;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        postRequest = PostRequest.builder()
                .title("title")
                .content("content")
                .build();

        user = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        anotherUser = User.builder()
                .email("another@email.com")
                .password("1234Password!")
                .nickname("nickname")
                .build();
    }

    RequestPostProcessor createPrincipal() {
        return SecurityMockMvcRequestPostProcessors.user(new PrincipalDetails(user));
    }

    String createPostRequestContent() throws JsonProcessingException {
        return objectMapper.writeValueAsString(postRequest);
    }

    String createPostResponseContent(Post post) throws JsonProcessingException {
        return objectMapper.writeValueAsString(PostResponse.of(post));
    }

    @DisplayName("게시글을 작성할 경우 HTTP 상태코드 201이 반환된다.")
    @Test
    void write() throws Exception {
        given(postService.write(any())).willReturn(any());

        MockHttpServletRequestBuilder content = post(POST_API_URI)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPostRequestContent());

        mockMvc.perform(content)
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("존재하는 게시글을 조회할 경우 HTTP 상태코드 200과 PostResponse 를 반환한다.")
    @Test
    void getExistPost() throws Exception {
        Post post = PostRequest.toEntity(postRequest, user);

        given(postService.findById(any())).willReturn(post);

        MockHttpServletRequestBuilder requestBuilder = get(POST_API_URI + "/{id}", postId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createPostResponseContent(post)));
    }

    @DisplayName("존재하지 않은 게시글을 조회할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void getNonExistPost() throws Exception {
        given(postService.findById(any())).willThrow(POST_NOT_FOUND_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = get(POST_API_URI + "/{id}", postId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("게시글을 찾을 수 없습니다."));
    }

    @DisplayName("사용자가 본인이 작성한 게시글를 수정할 경우 HTTP 상태코드 200과 PostResponse 를 반환한다.")
    @Test
    void modifyMyPost() throws Exception {
        Post myPost = PostRequest.toEntity(postRequest, user);

        given(postService.findById(any())).willReturn(myPost);

        willDoNothing().given(postService).modify(myPost, postRequest);

        MockHttpServletRequestBuilder requestBuilder = put(POST_API_URI + "/{id}", postId)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPostRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createPostResponseContent(myPost)));
    }

    @DisplayName("사용자가 본인이 작성하지 않은 포스트를 수정할 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void modifyAnotherUserPost() throws Exception {
        Post anotherUserPost = PostRequest.toEntity(postRequest, anotherUser);

        given(postService.findById(any())).willReturn(anotherUserPost);

        MockHttpServletRequestBuilder requestBuilder = put(POST_API_URI + "/{id}", postId)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPostRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("접근 권한이 없습니다."));
    }

    @DisplayName("사용자가 존재하지 않은 게시글을 수정할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void modifyNonExistPost() throws Exception {
        given(postService.findById(any())).willThrow(POST_NOT_FOUND_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = put(POST_API_URI + "/{id}", postId)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPostRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("게시글을 찾을 수 없습니다."));
    }

    @DisplayName("사용자가 본인이 작성한 게시글를 삭제할 경우 HTTP 상태코드 200을 반환한다.")
    @Test
    void deleteMyPost() throws Exception {
        Post myPost = PostRequest.toEntity(postRequest, user);

        given(postService.findById(any())).willReturn(myPost);
        willDoNothing().given(postService).deleteById(postId);

        MockHttpServletRequestBuilder requestBuilder = delete(POST_API_URI + "/{id}", postId)
                .with(createPrincipal());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("사용자가 본인이 작성하지 않은 포스트를 삭제할 경우 HTTP 상태코드 401과 메시지를 반환한다.")
    @Test
    void deleteAnotherUserPost() throws Exception {
        Post anotherUserPost = PostRequest.toEntity(postRequest, anotherUser);

        given(postService.findById(any())).willReturn(anotherUserPost);

        MockHttpServletRequestBuilder requestBuilder = delete(POST_API_URI + "/{id}", postId)
                .with(createPrincipal());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("접근 권한이 없습니다."));
    }

    @DisplayName("사용자가 존재하지 않은 게시글을 삭제할 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void deleteNonExistPost() throws Exception {
        given(postService.findById(any())).willThrow(POST_NOT_FOUND_EXCEPTION);

        MockHttpServletRequestBuilder requestBuilder = delete(POST_API_URI + "/{id}", postId)
                .with(createPrincipal());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("게시글을 찾을 수 없습니다."));
    }
}