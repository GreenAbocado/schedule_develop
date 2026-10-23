package com.greenabocado.scheduledevelop.common.interceptor;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        /* 해당 세션이 없거나 존재하지만 인증 상태가 없을 경우 */
        if (session == null || session.getAttribute(SessionConst.MEMBER_ID) == null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
        return true;
    }
}