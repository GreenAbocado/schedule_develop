package com.greenabocado.scheduledevelop.dto.schedule;

import com.greenabocado.scheduledevelop.dto.common.Writer;
import com.greenabocado.scheduledevelop.entity.Schedule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
@Getter
public class ScheduleDetailResponse{

    private final Long id;
    private final String title;
    private final String content;
    private final boolean editable;
    private final Writer writer;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ScheduleDetailResponse of(Schedule schedule, boolean editable) {
        return new ScheduleDetailResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                editable,
                new Writer(schedule.getMember().getId(), schedule.getMember().getName()),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt());
    }
}