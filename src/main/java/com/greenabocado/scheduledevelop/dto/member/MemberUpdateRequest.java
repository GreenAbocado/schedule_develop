package com.greenabocado.scheduledevelop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import static com.greenabocado.scheduledevelop.dto.DtoValidationMessage.*;

public record MemberUpdateRequest(
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
        String email
) {
}