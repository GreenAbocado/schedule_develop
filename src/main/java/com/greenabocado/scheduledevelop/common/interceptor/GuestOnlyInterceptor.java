package com.greenabocado.scheduledevelop.common.interceptor;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/* 로그인한 사용자가 로그인, 회원가입 못하도록 필터링 */
public class GuestOnlyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(SessionConst.MEMBER_ID) != null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_ALREADY);
        }

        return true;
    }
}
