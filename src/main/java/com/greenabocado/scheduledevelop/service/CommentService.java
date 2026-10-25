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
import java.util.List;
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

    @Transactional(readOnly=true)
    public List<CommentResponse> getAllCommentsInSchedule(Long sessionMemberId, Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }
        /* 데이터 후처리(editable 확인)가 필요해서 DTO 프로젝션이 아닌 fetch join으로 N+1 방지 */
        return commentRepository.findAllByScheduleIdWithMember(scheduleId).stream()
                .map((comment)->
                        CommentResponse.of(comment, comment.getMember().getId().equals(sessionMemberId))).toList();
    }

    @Transactional
    public CommentResponse updateContent(Long sessionMemberId, Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(COMMENT_NOT_FOUND));
        if (!comment.getMember().getId().equals(sessionMemberId)) {
            throw new CustomException(COMMENT_WRITER_FORBIDDEN);
        }
        comment.updateContent(request.content());
        return CommentResponse.of(comment, true);
    }
}
