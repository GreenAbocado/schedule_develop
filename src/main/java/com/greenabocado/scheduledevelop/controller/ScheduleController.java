package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.dto.common.PageResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleRequest;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleDetailResponse;
import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.service.ScheduleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
@Validated
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDetailResponse> createSchedule(
            @SessionAttribute(name= SessionConst.MEMBER_ID) Long memberId, @RequestBody @Valid ScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.createSchedule(memberId, request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ScheduleSummaryResponse>> getAllSchedules(
            @RequestParam(defaultValue="0") @Min(0) int page,
            @RequestParam(defaultValue="10") @Min(1) @Max(20) int size
    ) {
        return ResponseEntity.ok().body(scheduleService.getAllSchedules(page, size));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule(
            @SessionAttribute(required=false, name=SessionConst.MEMBER_ID) Long memberId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok().body(scheduleService.getSchedule(memberId, scheduleId));
    }

    @GetMapping("/me")
    public ResponseEntity<PageResponse<ScheduleSummaryResponse>> getMySchedules(
            @SessionAttribute(name=SessionConst.MEMBER_ID) Long memberId,
            @RequestParam(defaultValue="0") @Min(0) int page,
            @RequestParam(defaultValue="10") @Min(1) @Max(20) int size
    ) {
        return ResponseEntity.ok().body(scheduleService.getMyAllSchedules(memberId, page, size));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> updateMySchedule(
            @SessionAttribute(name=SessionConst.MEMBER_ID) Long memberId,
            @PathVariable Long scheduleId, @RequestBody @Valid ScheduleRequest request) {
        return ResponseEntity.ok().body(scheduleService.updateMySchedule(memberId, scheduleId, request));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteMySchedule(
            @SessionAttribute(name=SessionConst.MEMBER_ID) Long memberId, @PathVariable Long scheduleId) {
        scheduleService.deleteMySchedule(memberId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}