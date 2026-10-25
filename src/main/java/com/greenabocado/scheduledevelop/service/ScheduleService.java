package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import com.greenabocado.scheduledevelop.dto.common.PageResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleRequest;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleDetailResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.entity.Schedule;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.CommentRepository;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import com.greenabocado.scheduledevelop.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ScheduleDetailResponse createSchedule(Long memberId, ScheduleRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = Schedule.of(request.title(), request.content(), member);
        scheduleRepository.save(schedule);
        log.info("일정 생성 scheduleId={}", schedule.getId());
        return ScheduleDetailResponse.of(schedule, true);
    }

    /* findAll(pageable)을 쓰면 Page<Schedule>을 받아
        이후 Schedule -> ScheduleSummaryResponse(DTO) 변환 시, 지연 로딩으로 N+1 문제 발생 (회원 이름 접근)
        DTO 프로젝션으로 1번의 쿼리로 조인하여 해결 */
    @Transactional(readOnly=true)
    public PageResponse<ScheduleSummaryResponse> getAllSchedules(int page, int size) {
        Pageable pageable = getPageable(page, size);
        return PageResponse.from(scheduleRepository.findAllWithMemberName(pageable));
    }

    @Transactional(readOnly=true)
    public ScheduleDetailResponse getSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);

        // 2번째 파라미터: 작성자 여부(수정/삭제 버튼 표시 결정), memberId 널 가능(비로그인 사용자)
        return ScheduleDetailResponse.of(schedule, schedule.isWrittenBy(memberId));
    }

    /* 일정이 모두 같은 회원 프록시 객체를 참조하고 있으므로
       이후 DTO 변환에서 회원에 접근할 경우 1번의 쿼리로 회원을 조회하고 그 이후는 날라가지 않음 -> 간단하게 파생 메서드 사용 */
    @Transactional(readOnly=true)
    public PageResponse<ScheduleSummaryResponse> getMyAllSchedules(Long memberId, int page, int size) {
        Pageable pageable = getPageable(page, size);

        return PageResponse.from(scheduleRepository.findAllByMemberId(pageable, memberId)   // Page<Schedule>
                .map(ScheduleSummaryResponse::from));
    }

    @Transactional
    public ScheduleDetailResponse updateMySchedule(Long memberId, Long scheduleId, ScheduleRequest request) {
        Schedule schedule = getMyScheduleOrThrow(memberId, scheduleId);
        schedule.updateAll(request.title(), request.content());
        return ScheduleDetailResponse.of(schedule, true);
    }

    /* 파생 메서드로 댓글 목록 삭제 시 엔티티 상태 삭제로 변경 후 N번의 쿼리 추가 발생
     *  벌크 쿼리 1번으로 해결*/
    @Transactional
    public void deleteMySchedule(Long memberId, Long scheduleId) {
        Schedule schedule = getMyScheduleOrThrow(memberId, scheduleId);

        long commentDeletedCount = commentRepository.deleteAllByScheduleIdInBulk(scheduleId);
        log.info("일정 댓글 전체 {}건 삭제 scheduleId={}", commentDeletedCount, schedule.getId());

        scheduleRepository.delete(schedule);
        log.info("일정 삭제 scheduleId={}", schedule.getId());
    }

    private Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(
                Sort.Order.desc("updatedAt"),
                Sort.Order.desc("id"))); /* 수정일이 같을 경우 ID로 정렬 */
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