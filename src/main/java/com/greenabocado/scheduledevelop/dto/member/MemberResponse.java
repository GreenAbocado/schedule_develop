package com.greenabocado.scheduledevelop.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greenabocado.scheduledevelop.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {

    private final String name;
    private final String email;

    /* 본인 정보일 경우 계정 정보(email) 조회 가능 */
    public static MemberResponse fromPrivate(Member member) {
        return new MemberResponse(member.getName(), member.getEmail());
    }

    /* 타인 정보일 경우 기본 정보만 조회 가능 */
    public static MemberResponse fromPublic(Member member) {
        return new MemberResponse(member.getName(), null);
    }
}