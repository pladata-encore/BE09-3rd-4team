package com.smile.review.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // SecurityContextHolder에서 JWT 추출 (Spring Security 6 기준)
        String token = null;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() != null) {
            token = authentication.getCredentials().toString();
        }
        if (token != null && !token.isEmpty()) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
