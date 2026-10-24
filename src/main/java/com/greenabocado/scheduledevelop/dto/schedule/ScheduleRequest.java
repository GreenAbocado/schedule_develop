package com.greenabocado.scheduledevelop.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/* DTO가 엔티티보다 더 타이트한 검증 규칙 보유 */
public record ScheduleRequest(
        @NotBlank(message="제목은 필수 입력 항목입니다.")
        @Size(message="제목은 {max}자 이내로 입력해야 합니다.", max=30)
        String title,

        @Size(message="내용은 {max}자 이내로 입력해야 합니다.", max=500)
        String content
) {
    public ScheduleRequest {
        title = (title == null) ? null : title.trim();
        /* content의 경우 기본값(null)이든 빈 값이든 여기선 null로 처리 */
        content = (content == null || content.isBlank()) ? null : content.trim();
    }
}