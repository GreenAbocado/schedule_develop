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
}
