package io.devlog.devlog.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

}
