package com.greenabocado.scheduledevelop.service;

import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.dto.comment.CommentRequest;
import com.greenabocado.scheduledevelop.dto.comment.CommentResponse;
import com.greenabocado.scheduledevelop.entity.Comment;
import com.greenabocado.scheduledevelop.entity.Schedule;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.CommentRepository;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import com.greenabocado.scheduledevelop.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.greenabocado.scheduledevelop.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CommentResponse createComment(Long memberId, Long scheduleId, CommentRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new CustomException(SCHEDULE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        Comment comment = commentRepository.save(Comment.of(request.content(), member, schedule));
        return CommentResponse.of(comment, true);
    }
}
