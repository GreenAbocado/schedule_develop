package com.greenabocado.scheduledevelop.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/* 중복되는 DTO 메시지 관리 (도메인이 적어 나누지 않고 한 곳에서 관리) */
@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
public class DtoValidationMessage {

    /* member */
    public static final String MEMBER_NAME_INVALID = "이름은 필수 입력 항목입니다.";
    public static final String MEMBER_EMAIL_INVALID = "이메일은 필수 입력 항목입니다.";
    public static final String MEMBER_PASSWORD_INVALID = "비밀번호는 필수 입력 항목입니다.";

    public static final String MEMBER_NAME_PATTERN_MSG = "이름은 한글, 영문 조합 20자 이내로 입력해야 합니다.";
    public static final String MEMBER_EMAIL_PATTERN_MSG = "이메일은 올바른 형식으로 50자 이내로 입력해야 합니다.";
    public static final String MEMBER_PASSWORD_PATTERN_MSG = "비밀번호는 영문, 숫자, 특수문자 하나 이상 조합 8~16자여야 합니다.";

    public static final String MEMBER_NAME_REGEX = "^[가-힣a-zA-Z]{1,20}$";
    public static final String MEMBER_EMAIL_REGEX = "^(?=.{1,50}$)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String MEMBER_PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$";
}
