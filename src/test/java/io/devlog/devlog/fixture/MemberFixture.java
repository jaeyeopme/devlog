package io.devlog.devlog.fixture;

import io.devlog.devlog.member.dto.MemberDto;

public class MemberFixture {

    public static final MemberDto MEMBER_REQUEST = MemberDto
            .builder()
            .email("member@email.com")
            .password("Member1234!")
            .nickname("member")
            .build();

}
