package com.greenabocado.scheduledevelop.dto.schedule;

import com.greenabocado.scheduledevelop.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleSummaryResponse(Long id,
                                      String title,
                                      LocalDateTime updatedAt,
                                      String writerName) {

    public static ScheduleSummaryResponse from(Schedule schedule) {
        return new ScheduleSummaryResponse(schedule.getId(),
                schedule.getTitle(),
                schedule.getUpdatedAt(),
                schedule.getMember().getName());
    }
}