package io.devlog.devlog.member.service;

import io.devlog.devlog.member.domain.entity.Member;
import io.devlog.devlog.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GeneralMemberService implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void registration(Member member) {
        memberRepository.save(member);
    }

}
