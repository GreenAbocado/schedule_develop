package com.greenabocado.scheduledevelop.dto.member;

import jakarta.validation.constraints.NotBlank;
import static com.greenabocado.scheduledevelop.dto.DtoValidationMessage.*;

/* 엔티티와 단순 비교 작업이므로 null 및 공백만 체크 후 서비스에서 쳐냄 */
public record LoginRequest(
        @NotBlank(message=INVALID_EMAIL) String email,
        @NotBlank(message=INVALID_PASSWORD) String password
) {
}