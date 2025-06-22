package com.smile.recommendservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* Spring Security 설정의 중심으로, recommend-service가 API Gateway 기반 인증 구조에서
정상적으로 보안 필터링을 적용받기 위한 핵심 구성 클래스 */
/*Spring Security 전반 설정

인증 필터 등록, 권한 설정, 경로별 보안 규칙 정의*/
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session
                            -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(exception ->
                            exception
                                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                    )
                    .authorizeHttpRequests(auth ->
                            auth.anyRequest().authenticated()
                    )

                    // 기존 JWT 검증 필터 대신, Gateway가 전달한 헤더를 이용하는 필터 추가
                    .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        @Bean
        public HeaderAuthenticationFilter headerAuthenticationFilter() {
            return new HeaderAuthenticationFilter();
        }

    }