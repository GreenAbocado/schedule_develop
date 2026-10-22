package com.greenabocado.scheduledevelop.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/* 중복되는 DTO 메시지 관리 (도메인이 적어 나누지 않고 한 곳에서 관리) */
@RequiredArgsConstructor(access= AccessLevel.PRIVATE)
public class DtoValidationMessage {

    /* member */
    public static final String INVALID_EMAIL = "이메일은 필수 입력 항목입니다.";
    public static final String INVALID_PASSWORD = "비밀번호는 필수 입력 항목입니다.";


}
