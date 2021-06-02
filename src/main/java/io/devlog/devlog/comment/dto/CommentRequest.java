package io.devlog.devlog.comment.dto;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.user.domain.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private Long postId;

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

    public static Comment toEntity(CommentRequest commentRequest, Post post, User author) {
        return Comment.builder()
                .content(commentRequest.getContent())
                .post(post)
                .author(author)
                .build();
    }

}
