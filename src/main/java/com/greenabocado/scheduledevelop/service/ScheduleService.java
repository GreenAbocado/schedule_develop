package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleRequest;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleDetailResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.entity.Schedule;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import com.greenabocado.scheduledevelop.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ScheduleDetailResponse createSchedule(Long memberId, ScheduleRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = Schedule.of(request.title(), request.content(), member);
        scheduleRepository.save(schedule);
        log.info("일정 생성 scheduleId={}", schedule.getId());
        return ScheduleDetailResponse.of(schedule, true);
    }

    @Transactional(readOnly=true)
    public List<ScheduleSummaryResponse> getAllSchedules() {
        return scheduleRepository.findAllWithMemberName();
        /* findAll() 후에 각 member 접근 시 N+1, JPQL로 조인 후 1번의 조회로 DTO 프로젝션 */
    }

    @Transactional(readOnly=true)
    public ScheduleDetailResponse getSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);

        // 2번째 파라미터: 작성자 여부(수정/삭제 버튼 표시 결정), memberId 널 가능(비로그인 사용자)
        return ScheduleDetailResponse.of(schedule, schedule.isWrittenBy(memberId));
    }

    @Transactional(readOnly=true)
    public List<ScheduleSummaryResponse> getMyAllSchedules(Long memberId) {
        /* findByMemberId() 후 DTO에서 작성자 이름 조회할 때 처음에만 쿼리를 날려 member 가져옴 (스케줄간 동일한 멤버 프록시 객체) */
        return scheduleRepository.findAllByMemberId(memberId).stream()
                .map(ScheduleSummaryResponse::from).toList();
    }

    @Transactional
    public ScheduleDetailResponse updateMySchedule(Long memberId, Long scheduleId, ScheduleRequest request) {
        Schedule schedule = getMyScheduleOrThrow(memberId, scheduleId);
        schedule.updateAll(request.title(), request.content());
        return ScheduleDetailResponse.of(schedule, true);
    }

    @Transactional
    public void deleteMySchedule(Long memberId, Long scheduleId) {
        Schedule schedule = getMyScheduleOrThrow(memberId, scheduleId);
        scheduleRepository.delete(schedule);
        log.info("일정 삭제 scheduleId={}", schedule.getId());
    }

    /* 수정/삭제 시 스케줄 작성자와 세션 회원 정보 일치 여부 확인 */
    private Schedule getMyScheduleOrThrow(Long memberId, Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);

        if (!schedule.isWrittenBy(memberId)) {
            throw new CustomException(ErrorCode.SCHEDULE_WRITER_FORBIDDEN);
        }
        return schedule;
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}