package io.devlog.devlog.comment.dto;

import io.devlog.devlog.comment.domain.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentResponse {

    private final String content;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .build();
    }

}
