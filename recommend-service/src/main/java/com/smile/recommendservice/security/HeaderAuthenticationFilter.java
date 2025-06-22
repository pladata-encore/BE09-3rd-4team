package com.smile.recommendservice.security;

import com.smile.recommendservice.dto.UserDto;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String gender = request.getHeader("X-User-Gender");
        String ageStr = request.getHeader("X-User-Age");


        if (userId != null) {
            Integer age = null;
            try {
                if (ageStr != null) {
                    age = Integer.parseInt(ageStr);
                }
            } catch (NumberFormatException e) {
                log.warn("X-User-Age 헤더 값이 정수가 아닙니다: {}", ageStr);
            }

            // ✅ UserDto 생성
            UserDto userDto = UserDto.builder()
                    .userId(userId)
                    .gender(gender)
                    .age(age)
                    .role("USER")
                    .build();

            // ✅ UserDetailsWrapper 생성
            UserDetailsWrapper userDetails = new UserDetailsWrapper(userDto);

            // ✅ 인증 객체 생성 (권한 포함)
            PreAuthenticatedAuthenticationToken authentication =
                    new PreAuthenticatedAuthenticationToken(
                            userDetails,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            // ✅ SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("인증 헤더 수신: userId={}, gender={}, age={}", userId, gender, ageStr);
            log.info("🎯 사용자 인증 완료: {}", userDto); // 너가 이미 추가한 거

        }

        filterChain.doFilter(request, response);
    }
}
