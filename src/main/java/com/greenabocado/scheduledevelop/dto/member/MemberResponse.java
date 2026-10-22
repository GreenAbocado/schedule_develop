package com.greenabocado.scheduledevelop.dto.member;

import com.greenabocado.scheduledevelop.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
public class MemberResponse {

    private final String name;
    private final String email;

    public static MemberResponse from (Member member) {
        return new MemberResponse(member.getName(), member.getEmail());
    }
}