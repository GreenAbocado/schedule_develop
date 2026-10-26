package com.greenabocado.scheduledevelop.common.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.validation.FieldError;

/* 응답에 필요한 FiledError 필드 캡슐화 (에러 응답에 같이 반환) */
@Getter
public class FieldErrorResponse {
    private final String name;
    private final String message;

    private FieldErrorResponse(String name, String message) {
        this.name = name;
        this.message = message;
    }

    /* FieldError에서 응답에 필요한 요소만 추출 */
    public static FieldErrorResponse from(FieldError fieldError) {
        return new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
    }

    /* ConstraintViolation에서 응답에 필요한 요소만 추출 */
    public static FieldErrorResponse from(ConstraintViolation<?> constraintViolation) {
        return new FieldErrorResponse(constraintViolation.getPropertyPath().toString(),
                constraintViolation.getMessage());
    }
}