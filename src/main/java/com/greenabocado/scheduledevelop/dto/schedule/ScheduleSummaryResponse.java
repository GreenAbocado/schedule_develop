package com.greenabocado.scheduledevelop.dto.schedule;

import com.greenabocado.scheduledevelop.entity.Schedule;

import java.time.LocalDateTime;

/* DTO 프로젝션 시 전체 생성자로 생성 */
public record ScheduleSummaryResponse(Long id,
                                      String title,
                                      String summaryContent,
                                      LocalDateTime updatedAt,
                                      String writerName) {

    /* 일정 목록 반환 시 각 일정 본문은 잘라서 30자만 반환 */
    public ScheduleSummaryResponse {
        summaryContent = (summaryContent == null) ? null :
                (summaryContent.length() < 30) ? summaryContent : summaryContent.substring(0, 30);
    }

    /* 서비스에서 변환 시 정적 팩토리 사용 (본인 일정 조회) */
    public static ScheduleSummaryResponse from(Schedule schedule) {
        return new ScheduleSummaryResponse(schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUpdatedAt(),
                schedule.getMember().getName());
    }
}