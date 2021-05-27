package io.devlog.devlog.member.controller;

import io.devlog.devlog.member.dto.MemberDto;
import io.devlog.devlog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static io.devlog.devlog.commons.HttpStatusResponseEntity.RESPONSE_CREATED;
import static io.devlog.devlog.commons.HttpStatusResponseEntity.RESPONSE_OK;
import static io.devlog.devlog.member.controller.MemberController.MEMBER_API_URI;
import static io.devlog.devlog.member.exception.MemberResponseStatusException.DUPLICATED_EMAIL_EXCEPTION;

@RequestMapping(MEMBER_API_URI)
@RequiredArgsConstructor
@RestController
public class MemberController {

    public static final String MEMBER_API_URI = "/api/members";

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<HttpStatus> registration(@RequestBody MemberDto memberDto) {
        if (memberService.isDuplicatedEmail(memberDto.getEmail()))
            throw DUPLICATED_EMAIL_EXCEPTION;

        memberService.registration(MemberDto.toEntity(memberDto, passwordEncoder));

        return RESPONSE_CREATED;
    }

    @PostMapping("/duplicated/{email}")
    public ResponseEntity<HttpStatus> checkEmailDuplicate(@PathVariable String email) {
        if (memberService.isDuplicatedEmail(email))
            throw DUPLICATED_EMAIL_EXCEPTION;

        return RESPONSE_OK;
    }

}
