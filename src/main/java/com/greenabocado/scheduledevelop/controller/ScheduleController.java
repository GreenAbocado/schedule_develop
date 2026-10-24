package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleCreateRequest;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleDetailResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDetailResponse> createSchedule(
            @SessionAttribute(name= SessionConst.MEMBER_ID) Long memberId, @RequestBody @Valid ScheduleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.createSchedule(memberId, request));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleSummaryResponse>> getAllSchedules() {
        return ResponseEntity.ok().body(scheduleService.getAllSchedules());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule(
            @SessionAttribute(required=false, name=SessionConst.MEMBER_ID) Long memberId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok().body(scheduleService.getSchedule(memberId, scheduleId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ScheduleSummaryResponse>> getMySchedules(
            @SessionAttribute(name=SessionConst.MEMBER_ID) Long memberId) {
        return ResponseEntity.ok().body(scheduleService.getMyAllSchedules(memberId));
    }
}
