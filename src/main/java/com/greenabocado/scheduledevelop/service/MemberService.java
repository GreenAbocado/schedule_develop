package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.dto.member.MemberCreateRequest;
import com.greenabocado.scheduledevelop.dto.member.MemberServiceResult;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.greenabocado.scheduledevelop.common.exception.ErrorCode.USER_EMAIL_DUPLICATED;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberServiceResult register(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new CustomException(USER_EMAIL_DUPLICATED);
        }
        /* 이후 해시 함수 적용 */
        Member member = memberRepository.save(Member.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password()).build());

        return MemberServiceResult.from(member);
    }
}
