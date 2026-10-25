package com.greenabocado.scheduledevelop.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank @Size(message="댓글은 {max}자 이내로 입력해야 합니다.", max=250)
        String content
) {
    public CommentRequest {
        content = (content==null) ? null : content.trim();
    }
}