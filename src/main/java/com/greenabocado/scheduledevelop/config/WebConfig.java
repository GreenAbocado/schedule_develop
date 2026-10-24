package com.greenabocado.scheduledevelop.config;

import com.greenabocado.scheduledevelop.common.interceptor.AdminInterceptor;
import com.greenabocado.scheduledevelop.common.interceptor.GuestOnlyInterceptor;
import com.greenabocado.scheduledevelop.common.interceptor.AuthenticationInterceptor;
import com.greenabocado.scheduledevelop.common.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**")
                .order(1);

        /* 로그인 사용자만 허용 */
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**") // 허용 여부 인터셉터가 결정
                .order(2);

        /* 비로그인 사용자만 허용 (회원가입, 로그인) */
        registry.addInterceptor(new GuestOnlyInterceptor())
                .addPathPatterns("/members", "/members/login")
                .order(3);

        /* 관리자 권한 확인 */
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .order(4);
    }
}