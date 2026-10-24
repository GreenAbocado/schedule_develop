package com.greenabocado.scheduledevelop.common.interceptor;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.common.exception.CustomException;
import com.greenabocado.scheduledevelop.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.regex.Pattern;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (AllowedApi.isAllowed(request)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        /* 해당 세션이 없거나 존재하지만 인증 상태가 없을 경우 */
        if (session == null || session.getAttribute(SessionConst.MEMBER_ID) == null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
        return true;
    }

    /* HTTP 메서드까지 비교해야해서 인터셉터 내부에서 허용 여부 결정 */
    @RequiredArgsConstructor
    private enum AllowedApi {

        POST_REGISTER(HttpMethod.POST, Pattern.compile("/members")),
        POST_LOGIN(HttpMethod.POST, Pattern.compile("/members/login")),
        GET_ONE_MEMBER(HttpMethod.GET, Pattern.compile("/members/\\d+")),
        GET_SCHEDULES(HttpMethod.GET, Pattern.compile("/schedules")),
        GET_ONE_SCHEDULE(HttpMethod.GET, Pattern.compile("/schedules/\\d+"));

        private final HttpMethod method;
        private final Pattern uri;

        /* 매 요청시 배열 생성 방지 */
        private static final List<AllowedApi> ALLOW_APIS = List.of(AllowedApi.values());

        private static boolean isAllowed(HttpServletRequest request) {
            return ALLOW_APIS.stream().anyMatch((api)->
                    api.method.matches(request.getMethod()) &&
                    api.uri.matcher(request.getRequestURI()).matches());
        }
    }
}