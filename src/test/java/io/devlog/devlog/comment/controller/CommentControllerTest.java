package io.devlog.devlog.comment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.dto.CommentRequest;
import io.devlog.devlog.comment.dto.CommentResponse;
import io.devlog.devlog.comment.service.GeneralCommentService;
import io.devlog.devlog.error.ErrorResponse;
import io.devlog.devlog.error.comment.CommentNotFoundException;
import io.devlog.devlog.error.post.PostNotFoundException;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.service.GeneralPostService;
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

import static io.devlog.devlog.comment.controller.CommentController.COMMENT_API_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private final Long commentId = 1L;

    @MockBean
    private GeneralPostService postService;

    @MockBean
    private GeneralCommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Post post;

    private User anotherUser;

    private CommentRequest commentRequest;

    RequestPostProcessor createPrincipal() {
        return SecurityMockMvcRequestPostProcessors.user(PrincipalDetails.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build());
    }

    String createCommentRequestContent() throws JsonProcessingException {
        return objectMapper.writeValueAsString(commentRequest);
    }

    String createPostResponseContent(Comment comment) throws JsonProcessingException {
        return objectMapper.writeValueAsString(CommentResponse.from(comment));
    }

    String createErrorResponseContent(String message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new ErrorResponse(message));
    }

    @BeforeEach
    void setUp() {
        commentRequest = CommentRequest.builder()
                .content("content")
                .build();

        user = User.builder()
                .email("email@email.com")
                .password("Password1234!")
                .nickname("nickname")
                .build();

        post = Post.builder()
                .title("title")
                .content("content")
                .author(user)
                .build();

        anotherUser = User.builder()
                .email("another@email.com")
                .password("1234Password!")
                .nickname("nickname")
                .build();
    }

    @DisplayName("???????????? ???????????? ???????????? ????????? ????????? ?????? HTTP ???????????? 201??? ????????????.")
    @Test
    void writeNewCommentWithExistPost() throws Exception {
        given(postService.findById(any())).willReturn(any());

        MockHttpServletRequestBuilder requestBuilder = post(COMMENT_API_URI)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("???????????? ???????????? ?????? ???????????? ????????? ????????? ?????? HTTP ???????????? 404??? ???????????? ????????????.")
    @Test
    void writeNewCommentWithNonExistPost() throws Exception {
        given(postService.findById(any())).willThrow(new PostNotFoundException(any()));

        MockHttpServletRequestBuilder requestBuilder = post(COMMENT_API_URI)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(createErrorResponseContent("???????????? ?????? ??? ????????????.")));
    }

    @DisplayName("???????????? ???????????? ????????? ????????? ?????? HTTP ???????????? 200??? CommentResponse ??? ????????????.")
    @Test
    void getExistComment() throws Exception {
        Comment comment = Comment.from(commentRequest, post, user);

        given(commentService.findById(any())).willReturn(comment);

        MockHttpServletRequestBuilder requestBuilder = get(COMMENT_API_URI + "/{id}", commentId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createPostResponseContent(comment)));
    }

    @DisplayName("???????????? ???????????? ?????? ????????? ????????? ?????? HTTP ???????????? 404??? ???????????? ????????????.")
    @Test
    void getNonExistComment() throws Exception {
        given(commentService.findById(any())).willThrow(new CommentNotFoundException(any()));

        MockHttpServletRequestBuilder requestBuilder = get(COMMENT_API_URI + "/{id}", commentId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(createErrorResponseContent("????????? ?????? ??? ????????????.")));
    }

    @DisplayName("???????????? ????????? ????????? ????????? ????????? ?????? HTTP ???????????? 200??? CommentResponse ??? ????????????.")
    @Test
    void modifyMyComment() throws Exception {
        Comment myComment = Comment.from(commentRequest, post, user);

        given(commentService.findById(any())).willReturn(myComment);

        MockHttpServletRequestBuilder requestBuilder = put(COMMENT_API_URI + "/{id}", commentId)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createPostResponseContent(myComment)));
    }

    @DisplayName("???????????? ????????? ???????????? ?????? ????????? ????????? ?????? HTTP ???????????? 403??? ???????????? ????????????.")
    @Test
    void modifyAnotherUserComment() throws Exception {
        Comment anotherUserComment = Comment.from(commentRequest, post, anotherUser);

        given(commentService.findById(any())).willReturn(anotherUserComment);

        MockHttpServletRequestBuilder requestBuilder = put(COMMENT_API_URI + "/{id}", commentId)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().json(createErrorResponseContent("?????? ????????? ????????????.")));
    }

    @DisplayName("???????????? ???????????? ?????? ????????? ????????? ?????? HTTP ???????????? 404??? ???????????? ????????????.")
    @Test
    void modifyNonExistPost() throws Exception {
        given(commentService.findById(any())).willThrow(new CommentNotFoundException(any()));

        MockHttpServletRequestBuilder requestBuilder = put(COMMENT_API_URI + "/{id}", commentId)
                .with(createPrincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentRequestContent());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(createErrorResponseContent("????????? ?????? ??? ????????????.")));
    }

    @DisplayName("???????????? ????????? ????????? ????????? ????????? ?????? HTTP ???????????? 200??? ????????????.")
    @Test
    void deleteMyComment() throws Exception {
        Comment myComment = Comment.from(commentRequest, post, user);

        given(commentService.findById(any())).willReturn(myComment);

        MockHttpServletRequestBuilder requestBuilder = delete(COMMENT_API_URI + "/{id}", commentId)
                .with(createPrincipal());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("???????????? ???????????? ?????? ????????? ????????? ?????? HTTP ???????????? 404??? ???????????? ????????????.")
    @Test
    void deleteNonExistComment() throws Exception {
        given(commentService.findById(commentId)).willThrow(new CommentNotFoundException(any()));

        MockHttpServletRequestBuilder requestBuilder = delete(COMMENT_API_URI + "/{id}", commentId)
                .with(createPrincipal());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(createErrorResponseContent("????????? ?????? ??? ????????????.")));
    }

    @DisplayName("???????????? ????????? ???????????? ?????? ????????? ????????? ?????? HTTP ???????????? 403??? ???????????? ????????????.")
    @Test
    void deleteAnotherUserComment() throws Exception {
        Comment anotherUserComment = Comment.from(commentRequest, post, anotherUser);

        given(commentService.findById(any())).willReturn(anotherUserComment);

        MockHttpServletRequestBuilder requestBuilder = delete(COMMENT_API_URI + "/{id}", commentId)
                .with(createPrincipal());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().json(createErrorResponseContent("?????? ????????? ????????????.")));
    }

}