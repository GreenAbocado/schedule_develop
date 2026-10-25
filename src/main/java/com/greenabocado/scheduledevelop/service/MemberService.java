package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.config.PasswordEncoder;
import com.greenabocado.scheduledevelop.dto.member.*;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.CommentRepository;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import com.greenabocado.scheduledevelop.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.greenabocado.scheduledevelop.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberServiceResult register(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new CustomException(USER_EMAIL_DUPLICATED);
        }
        Member member = memberRepository.save(
                Member.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())).build()
        );
        log.info("회원 생성 memberId={}", member.getId());
        return MemberServiceResult.from(member);
    }

    @Transactional(readOnly=true)
    public MemberServiceResult login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(()-> new CustomException(USER_LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(USER_LOGIN_FAILED);
        }
        return MemberServiceResult.from(member);
    }

    @Transactional(readOnly=true)
    public MemberResponse getPrivateInfo(Long myId) {
        return MemberResponse.fromPrivate(findMemberById(myId));
        /* 회원 탈퇴(DB 삭제) 이후 세션 만료 전에 해당 요청이 들어오면 예외가 발생하는 등 특정 케이스 존재 */
    }

    @Transactional(readOnly=true)
    public MemberResponse getPublicInfo(Long id) {
        return MemberResponse.fromPublic(findMemberById(id));
    }

    @Transactional(readOnly=true)
    public List<MemberResponse> getAllMembersByAdmin() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::fromPrivate).toList();
    }

    @Transactional
    public MemberResponse updateMyInfo(Long id, MemberUpdateRequest request) {
        Member member = findMemberById(id);
        /* 다른 사용자 이메일과 중복 */
        if (!member.getEmail().equals(request.email()) && memberRepository.existsByEmail(request.email())) {
            throw new CustomException(USER_EMAIL_DUPLICATED);
        }
        member.changeInfo(request.name(), request.email());
        return MemberResponse.fromPrivate(member);
    }

    @Transactional
    public void updatePassword(Long id, PasswordUpdateRequest request) {
        Member member = findMemberById(id);
        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
            throw new CustomException(USER_PASSWORD_MISMATCH);
        }
        member.changePassword(request.newPassword());
    }

    /* 삭제 파생메서드일 경우, 조회 후 삭제 상태로 변경하여 N건의 쿼리
     * 벌크 쿼리를 통해 1번의 쿼리로 관련 스케줄이나 댓글 삭제
     * 벌크 쿼리 실행 시점에 1차 캐시가 비워져 있으므로 사실상 flush, clear 불필요 */
    @Transactional
    public void deleteMyAccount(Long id) {
        long commentDeletedCount = commentRepository.deleteAllByMemberIdInBulk(id);
        log.info("회원 댓글 전체 {}건 삭제 memberId={}", commentDeletedCount, id);

        long scheduleDeletedCount = scheduleRepository.deleteAllByMemberIdInBulk(id);
        log.info("회원 일정 전체 {}건 삭제 memberId={}", scheduleDeletedCount, id);

        memberRepository.deleteById(id);
        log.info("회원 탈퇴 memberId={}", id);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
    }
}