package io.devlog.devlog.fixture;

import io.devlog.devlog.member.domain.entity.Member;
import io.devlog.devlog.member.dto.MemberDto;

public class MemberFixture {

    public static final Member NEW_MEMBER = Member
            .builder()
            .email("newMember@email.com")
            .password("newMember1234!")
            .nickname("newMember")
            .build();

    public static final MemberDto MEMBER_REGISTRATION_REQUEST = MemberDto
            .builder()
            .email("newMember@email.com")
            .password("newMember1234!")
            .nickname("newMember")
            .build();

}
