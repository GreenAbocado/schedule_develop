package com.greenabocado.scheduledevelop.dto.comment;

import com.greenabocado.scheduledevelop.dto.common.Writer;
import com.greenabocado.scheduledevelop.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final Writer writer;
    private final boolean editable;
    private final LocalDateTime updatedAt;

    public static CommentResponse of(Comment comment, boolean editable) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                /* 전체 조회에서 DTO 변환 시 getName()으로 인해 N+1가능 (주의) */
                new Writer(comment.getMember().getId(), comment.getMember().getName()),
                editable,
                comment.getUpdatedAt()
        );
    }
}