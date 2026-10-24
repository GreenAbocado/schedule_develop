package com.greenabocado.scheduledevelop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.greenabocado.scheduledevelop.dto.DtoValidationMessage.*;

/* 현재 비번은 비교용이므로 간단히 null 검사만, 새로운 비번은 엔티티에 들어갈 값이므로 전체 검사 */
public record PasswordUpdateRequest(
        @NotBlank(message=MEMBER_PASSWORD_INVALID) String currentPassword,

        @NotBlank(message=MEMBER_PASSWORD_INVALID)
        @Pattern(regexp=MEMBER_PASSWORD_REGEX, message=MEMBER_PASSWORD_PATTERN_MSG)
        String newPassword
) {
}
