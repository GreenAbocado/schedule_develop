package com.greenabocado.scheduledevelop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import static com.greenabocado.scheduledevelop.dto.DtoValidationMessage.*;

public record MemberCreateRequest(
        @NotBlank(message="이름은 필수 입력 항목입니다.")
        @Pattern(
                regexp="^[가-힣a-zA-Z]{1,20}$",
                message="이름은 한글, 영문 조합 20자 이내로 입력해야 합니다."
        )
        String name,

        @NotBlank(message=INVALID_EMAIL)
        @Pattern(
                regexp="^(?=.{1,50}$)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message="이메일은 올바른 형식으로 50자 이내로 입력해야 합니다."
        )
        String email,

        @NotBlank(message=INVALID_PASSWORD)
        @Pattern(
                regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
                message="비밀번호는 영문, 숫자, 특수문자 하나 이상 조합 8~16자여야 합니다."
        )
        String password) {

    /* 이름, 이메일 앞 뒤 공백 제거 */
    public MemberCreateRequest {
        name = (name == null) ? null : name.trim();
        email = (email == null) ? null : email.trim();
        /* 비밀번호 trim()은 공백으로 인해 사용자를 혼란시킬 수 있으므로 불가 */
    }
}