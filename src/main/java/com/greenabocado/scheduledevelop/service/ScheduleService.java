package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleCreateRequest;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleDetailResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.entity.Schedule;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import com.greenabocado.scheduledevelop.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ScheduleDetailResponse createSchedule(Long memberId, ScheduleCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = Schedule.of(request.title(), request.content(), member);
        scheduleRepository.save(schedule);
        return ScheduleDetailResponse.of(schedule, true);
    }

    @Transactional(readOnly=true)
    public List<ScheduleSummaryResponse> getAllSchedules() {
        return scheduleRepository.findAllWithMember();
        /* findAll() 후에 각 member 접근 시 N+1, JPQL로 조인 후 1번의 조회로 DTO 프로젝션 */
    }

    @Transactional(readOnly=true)
    public ScheduleDetailResponse getSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        // 단순 수정/삭제 버튼 화면 표시 여부 결정, memberId 널 가능(비로그인 사용자)
        boolean editable = schedule.getMember().getId().equals(memberId);
        return ScheduleDetailResponse.of(schedule, editable);
    }

    @Transactional(readOnly=true)
    public List<ScheduleSummaryResponse> getMyAllSchedules(Long memberId) {
        /* findByMemberId() 후 DTO에서 작성자 이름 조회할 때 처음에만 쿼리를 날려 member 가져옴 (스케줄간 동일한 멤버 프록시 객체) */
        return scheduleRepository.findByMemberId(memberId).stream()
                .map(ScheduleSummaryResponse::from).toList();
    }
}