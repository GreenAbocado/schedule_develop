package com.greenabocado.scheduledevelop.dto.member;

import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.entity.member.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* 회원가입, 로그인의 경우 서비스에서 반환받은 id, role을 컨트롤러에서 써야해서 DTO와 함께 감쌈 */
@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
@Getter
public class MemberServiceResult {
    private final Long id;
    private final Role role;
    private final MemberResponse memberResponse;

    public static MemberServiceResult from(Member member) {
        return new MemberServiceResult(member.getId(), member.getRole(), MemberResponse.fromPrivate(member));
    }
}