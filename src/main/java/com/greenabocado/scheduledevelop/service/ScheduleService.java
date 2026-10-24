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
}