package com.smile.userservice.config;

import com.smile.userservice.jwt.HeaderAuthenticationFilter;
import com.smile.userservice.jwt.RestAccessDeniedHandler;
import com.smile.userservice.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(restAuthenticationEntryPoint) // 인증 실패
                                .accessDeniedHandler(restAccessDeniedHandler) // 인가 실패
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST, "/users", "/auth/login", "/auth/refresh", "/auth/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/internal/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/users/**").permitAll()
                        .anyRequest().authenticated()
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
