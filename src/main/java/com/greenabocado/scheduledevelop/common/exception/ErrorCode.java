package com.greenabocado.scheduledevelop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /* 공통 */
    REQUEST_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "G001", "요청 형식이 잘못되었습니다."),
    REQUEST_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "G002", "입력값 검증에 실패하였습니다."),
    REQUEST_URL_INVALID(HttpStatus.NOT_FOUND, "G003", "URL 경로가 잘못되었습니다"),
    REQUEST_METHOD_INVALID(HttpStatus.METHOD_NOT_ALLOWED, "G004", "HTTP 메서드가 잘못되었습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "G005", "인증에 실패하였습니다."),
    AUTHENTICATION_ALREADY(HttpStatus.FORBIDDEN, "G006", "이미 로그인된 사용자입니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 내부 오류입니다."),

    /* 회원 */
    USER_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "U001", "로그인에 실패하였습니다"),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "U002", "중복된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U003", "해당 사용자가 존재하지 않습니다."),

    /* 관리자 */
    ADMIN_FORBIDDEN(HttpStatus.FORBIDDEN, "A001", "관리자 인가에 실패하였습니다"),

    /* 일정 */
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "해당 일정이 존재하지 않습니다"),
    SCHEDULE_WRITER_FORBIDDEN(HttpStatus.FORBIDDEN, "S002", "편집 권한이 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
