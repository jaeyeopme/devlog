package io.devlog.devlog.post.service;

import io.devlog.devlog.error.exception.PostNotFoundException;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.domain.repository.PostRepository;
import io.devlog.devlog.post.dto.PostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class GeneralPostServiceTest {

    @Mock
    PostRepository postRepository;
    @InjectMocks
    GeneralPostService postService;
    private PostRequest updatePostRequest;
    private Post post;

    @BeforeEach
    void setUp() {
        updatePostRequest = PostRequest.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();

        post = Post.builder()
                .title("title")
                .content("content")
                .build();
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    void writePost() {
        postService.write(post);

        then(postRepository).should(only()).save(any());
    }

    @DisplayName("존재하는 게시글의 ID로 게시글을 조회하는 경우 조회된 게시글을 반환한다.")
    @Test
    void findPostByExistPostId() {
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        Post findPost = postService.findById(any());

        assertThat(post).isEqualTo(findPost);
        then(postRepository).should(only()).findById(any());
    }

    @DisplayName("존재하지 않은 게시글의 ID로 게시글을 조회하는 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void findPostByNonExistPostId() {
        given(postRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findById(any()));

        then(postRepository).should(only()).findById(any());
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void modifyPost() {
        assertThat(post.getTitle()).isNotEqualTo(updatePostRequest.getTitle());
        assertThat(post.getContent()).isNotEqualTo(updatePostRequest.getContent());

        postService.modify(post, updatePostRequest);

        assertThat(post.getTitle()).isEqualTo(updatePostRequest.getTitle());
        assertThat(post.getContent()).isEqualTo(updatePostRequest.getContent());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deletePostByPostId() {
        postService.deleteById(any());

        then(postRepository).should(only()).deleteById(any());
    }

}