package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.dto.comment.CommentRequest;
import com.greenabocado.scheduledevelop.dto.comment.CommentResponse;
import com.greenabocado.scheduledevelop.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @SessionAttribute(name= SessionConst.MEMBER_ID) Long memberId,
            @PathVariable Long scheduleId, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(memberId, scheduleId, request));
    }

    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllCommentsInSchedule(
            /* 편집 가능 여부를 확인하기 위해 사용자의 세션 필요 */
            @SessionAttribute(required=false, name=SessionConst.MEMBER_ID) Long memberId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok().body(commentService.getAllCommentsInSchedule(memberId, scheduleId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateContent(
            @SessionAttribute(name=SessionConst.MEMBER_ID) Long memberId, @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok().body(commentService.updateContent(memberId, commentId, request));
    }
}
