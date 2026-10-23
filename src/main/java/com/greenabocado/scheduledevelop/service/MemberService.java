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
    public MemberServiceResult register(MemberRequest request) {
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
        /* 회원 탈퇴(DB 삭제) 이후 세션 만료 전에 해당 요청이 들어오면 예외가 발생하는 등 특정 케이스 존재 */
    }

    @Transactional(readOnly=true)
    public MemberResponse getPublicInfo(Long id) {
        return MemberResponse.fromPublic(memberRepository.findById(id)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND)));
    }

    @Transactional
    public MemberResponse updateMyInfo(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        /* 다른 사용자 이메일과 중복 */
        if (!member.getEmail().equals(request.email()) && memberRepository.existsByEmail(request.email())) {
            throw new CustomException(USER_EMAIL_DUPLICATED);
        }
        member.changeInfo(request.name(), request.email());
        member.changePassword(request.password());
        return MemberResponse.fromPrivate(member);
    }


}
