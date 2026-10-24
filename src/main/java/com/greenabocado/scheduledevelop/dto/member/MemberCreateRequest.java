package com.greenabocado.scheduledevelop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import static com.greenabocado.scheduledevelop.dto.DtoValidationMessage.*;

/* 생성, 정보 수정, 비밀번호 변경 모두 다르므로 분리 */
public record MemberCreateRequest(
        @NotBlank(message=MEMBER_NAME_INVALID)
        @Pattern(
                regexp=MEMBER_NAME_REGEX,
                message=MEMBER_NAME_PATTERN_MSG
        )
        String name,

        @NotBlank(message=MEMBER_EMAIL_INVALID)
        @Pattern(
                regexp=MEMBER_EMAIL_REGEX,
                message=MEMBER_EMAIL_PATTERN_MSG
        )
        String email,

        @NotBlank(message=MEMBER_PASSWORD_INVALID)
        @Pattern(
                regexp=MEMBER_PASSWORD_REGEX,
                message=MEMBER_PASSWORD_PATTERN_MSG
        )
        String password) {

    /* 이름, 이메일 앞 뒤 공백 제거 */
    public MemberCreateRequest {
        name = (name == null) ? null : name.trim();
        email = (email == null) ? null : email.trim();
        /* 비밀번호 trim()은 공백으로 인해 사용자를 혼란시킬 수 있으므로 불가 */
    }
}