package io.devlog.devlog.user.dto;

import io.devlog.devlog.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserResponse {

    private final String email;

    private final String nickname;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
