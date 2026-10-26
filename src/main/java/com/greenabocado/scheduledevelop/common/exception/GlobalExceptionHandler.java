package com.greenabocado.scheduledevelop.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.List;
import static com.greenabocado.scheduledevelop.common.exception.ErrorCode.*;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /* HTTP 요청 형식 오류 */
    @ExceptionHandler({HttpMessageNotReadableException.class,   // JSON 꺠짐, 타입 변환 오류
            MethodArgumentTypeMismatchException.class,  // 쿼리, 경로변수 타입 변환 실패
            MissingServletRequestParameterException.class}) // 필수 쿼리 누락 (현재는 필요X)
    public ResponseEntity<ErrorResponse> handleHttpFormatException(Exception e) {
        log.warn("[{}] {}", REQUEST_FORMAT_INVALID.getCode(), e.getClass().getSimpleName());
        return buildResponseEntity(REQUEST_FORMAT_INVALID);
    }

    /* DTO 검증 실패 (객체 생성 후 Bean Validation 적용) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldErrorResponse> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldErrorResponse::from).toList();

        return buildValidationFailResponseEntity(fieldErrors);
    }

    /* 쿼리 or 경로변수 검증 실패 (현재는 페이징 단위 검증에 적용) */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleQueryValidationException(ConstraintViolationException e) {
        List<FieldErrorResponse> fieldErrors = e.getConstraintViolations().stream()
                .map(FieldErrorResponse::from).toList();

        return buildValidationFailResponseEntity(fieldErrors);
    }

    /* 비즈니스 예외 */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode code = e.getErrorCode();
        log.warn("[{}] {}", code.getCode(), code.getMessage());
        return buildResponseEntity(code);
    }

    /* URL Not Found */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException() {
        log.warn("[{}]", REQUEST_URL_INVALID.getCode());
        return buildResponseEntity(REQUEST_URL_INVALID);
    }

    /* HTTP 메서드 불일치 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException() {
        log.warn("[{}]", REQUEST_METHOD_INVALID.getCode());
        return buildResponseEntity(REQUEST_METHOD_INVALID);
    }

    // 클라이언트 오류 발견시 추가 필요

    /* 서버 에러 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("[{}]", SERVER_ERROR.getCode(), e);
        return buildResponseEntity(SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorCode code) {
        return ResponseEntity.status(code.getStatus())
                .body(ErrorResponse.from(code));
    }

    private ResponseEntity<ErrorResponse> buildValidationFailResponseEntity(List<FieldErrorResponse> fieldErrors) {
        ErrorCode code = REQUEST_VALIDATION_FAILED;
        log.warn("[{}] {}건의 검증 실패", code.getCode(), fieldErrors.size());
        return ResponseEntity.status(code.getStatus())
                .body(ErrorResponse.of(code, fieldErrors));
    }
}