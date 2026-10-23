package com.greenabocado.scheduledevelop.common.interceptor;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import com.greenabocado.scheduledevelop.entity.member.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /* 인증 인터셉터 이후라 세션은 이미 존재 확인 */
        Role role = (Role)request.getSession(false).getAttribute(SessionConst.MEMBER_ROLE);

        if (role != Role.ADMIN) {
            throw new CustomException(ErrorCode.ADMIN_FORBIDDEN);
        }
        return true;
    }
}
