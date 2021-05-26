package io.devlog.devlog.member.dto;

import io.devlog.devlog.member.domain.entity.Member;
import lombok.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Value
public class MemberDto {

    String email;

    String password;

    String nickname;

    public static Member toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .build();
    }

}
