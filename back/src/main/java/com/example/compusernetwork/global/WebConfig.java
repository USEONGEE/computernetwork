package com.example.compusernetwork.global;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 세션 타임아웃 설정 (선택 사항)
    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                se.getSession().setMaxInactiveInterval(30 * 60); // 30분
            }
        });
    }
}
