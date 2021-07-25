package io.devlog.devlog.user.dto;

import io.devlog.devlog.user.domain.entity.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    private String email;
    private String nickname;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
