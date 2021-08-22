package io.devlog.devlog.comment.service;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.domain.repository.CommentRepository;
import io.devlog.devlog.comment.dto.CommentRequest;
import io.devlog.devlog.error.exception.CommentNotFoundException;
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
public class GeneralCommentServiceTest {

    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    GeneralCommentService commentService;
    private CommentRequest updateCommentRequest;
    private Comment comment;

    @BeforeEach
    void setUp() {
        updateCommentRequest = CommentRequest.builder()
                .content("updateContent")
                .build();

        comment = Comment.builder()
                .content("content")
                .build();
    }

    @DisplayName("댓글을 작성한다.")
    @Test
    void writeComment() {
        commentService.write(comment);

        then(commentRepository).should(only()).save(any());
    }

    @DisplayName("존재하는 댓글 ID로 댓글을 조회하는 경우 조회된 댓글을 반환한다.")
    @Test
    void findCommentByExistCommentId() {
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));

        Comment findComment = commentService.findById(any());

        assertThat(comment).isEqualTo(findComment);
        then(commentRepository).should(only()).findById(any());
    }

    @DisplayName("존재하지 않은 댓글 ID로 댓글을 조회하는 경우 HTTP 상태코드 404와 메시지를 반환한다.")
    @Test
    void findCommentByNonExistCommentId() {
        given(commentRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.findById(any()));

        then(commentRepository).should(only()).findById(any());
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void modifyComment() {
        assertThat(comment.getContent()).isNotEqualTo(updateCommentRequest.getContent());

        commentService.modify(comment, updateCommentRequest);

        assertThat(comment.getContent()).isEqualTo(updateCommentRequest.getContent());
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteCommentByCommentId() {
        commentService.deleteById(any());

        then(commentRepository).should(only()).deleteById(any());
    }

}
