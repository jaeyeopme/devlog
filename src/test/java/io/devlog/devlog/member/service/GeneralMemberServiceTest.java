package io.devlog.devlog.member.service;

import io.devlog.devlog.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.devlog.devlog.fixture.MemberFixture.MEMBER_REQUEST;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralMemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    private GeneralMemberService generalMemberService;

    @DisplayName("중복된 이메일이 없을 경우 FALSE 를 반환한다.")
    @Test
    void isDuplicatedEmail_Duplicated_False() {
        when(memberRepository.existsByEmail(any())).thenReturn(false);

        assertFalse(generalMemberService.isDuplicatedEmail(MEMBER_REQUEST.getEmail()));
    }

    @DisplayName("중복된 이메일이 있을 경우 TRUE 를 반환한다.")
    @Test
    void isDuplicatedEmail_NotDuplicated_True() {
        when(memberRepository.existsByEmail(any())).thenReturn(true);

        assertTrue(generalMemberService.isDuplicatedEmail(MEMBER_REQUEST.getEmail()));
    }

}