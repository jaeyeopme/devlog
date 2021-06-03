package io.devlog.devlog.user.dto;

import io.devlog.devlog.user.domain.entity.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class UserResponse {

    private final String email;
    private final String nickname;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
