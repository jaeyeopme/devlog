package io.devlog.devlog.post.dto;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.user.domain.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content;

    private User author;

    public static Post toEntity(PostRequest postRequest, User user) {
        return Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(user)
                .build();
    }

}
