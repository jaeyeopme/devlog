package io.devlog.devlog.comment.dto;

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

}
