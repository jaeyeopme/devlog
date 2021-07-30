package io.devlog.devlog.post.dto;

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

}
