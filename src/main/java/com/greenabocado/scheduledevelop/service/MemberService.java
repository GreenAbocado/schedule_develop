package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.dto.member.*;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.greenabocado.scheduledevelop.common.exception.ErrorCode.*;

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

    @Transactional(readOnly=true)
    public MemberServiceResult login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(()-> new CustomException(USER_LOGIN_FAILED));

        /* 암호화 적용 이후 변경 */
        if (!member.getPassword().equals(request.password())) {
            throw new CustomException(USER_LOGIN_FAILED);
        }
        return MemberServiceResult.from(member);
    }

    @Transactional(readOnly=true)
    public MemberResponse getPrivateInfo(Long myId) {
        return MemberResponse.fromPrivate(memberRepository.findById(myId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND)));
    }

    @Transactional(readOnly=true)
    public MemberResponse getPublicInfo(Long id) {
        return MemberResponse.fromPublic(memberRepository.findById(id)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND)));
    }
}
