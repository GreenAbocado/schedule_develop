package com.greenabocado.scheduledevelop.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = UUID.randomUUID().toString();
        MDC.put("uuid", uuid);  // 스레드 로컬에 저장

        log.info("REQUEST: [{} {}]", request.getMethod(), request.getRequestURI());
        return true; // 다음 인터셉터 혹은 핸들러 이동
    }

    /* 전역 예외처리까지 끝나고 디스패처 서블릿으로 돌아가기 전 실행 */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("RESPONSE: [{}]", response.getStatus());
        MDC.clear(); // 스레드풀로 돌아가기 전 스레드 로컬 비우기
    }
}
