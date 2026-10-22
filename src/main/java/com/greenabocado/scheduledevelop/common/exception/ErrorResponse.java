package com.greenabocado.scheduledevelop.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    /* DTO 검증 실패에만 에러 필드 목록 추가 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<FieldErrorResponse> fieldErrors;

    private ErrorResponse(String code, String message, List<FieldErrorResponse> fieldErrors) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse from(ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }

    /* DTO 검증 실패용 */
    public static ErrorResponse of(ErrorCode code, List<FieldErrorResponse> fieldErrors) {
        return new ErrorResponse(code.getCode(), code.getMessage(), fieldErrors);
    }
}
