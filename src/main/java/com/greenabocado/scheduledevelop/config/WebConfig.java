package com.greenabocado.scheduledevelop.config;

import com.greenabocado.scheduledevelop.common.interceptor.AlreadyAuthenticationInterceptor;
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


        /* API대로 변경 필요 */
        /* 인증 세션 존재 확인 (로그인한 사용자만 허용) */
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/members", "/members/login")
                .order(2);
        /* 인증 세션 미존재 확인 (비로그인 사용자만 허용) */
        registry.addInterceptor(new AlreadyAuthenticationInterceptor())
                .addPathPatterns("/members", "/members/login")
                .order(3);
    }
}