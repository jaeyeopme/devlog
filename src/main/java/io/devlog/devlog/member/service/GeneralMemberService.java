package io.devlog.devlog.member.service;

import io.devlog.devlog.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GeneralMemberService implements MemberService {

    private final MemberRepository memberRepository;

}
