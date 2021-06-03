package io.devlog.devlog.post.dto;

import io.devlog.devlog.post.domain.entity.Post;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class PostResponse {

    private final String title;
    private final String content;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

}
