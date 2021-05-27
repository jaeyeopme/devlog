package io.devlog.devlog.member.service;

import io.devlog.devlog.member.domain.entity.Member;

public interface MemberService {

    boolean isDuplicatedEmail(String email);

    void registration(Member member);

}
