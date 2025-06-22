package com.smile.recommendservice.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
/*인증 실패 시 반환할 에러 응답 커스터마이징

예: 401 Unauthorized 응답 내용 정의*/
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 고정된 메시지 반환
        String jsonResponse = "{\"error\": \"Unauthorized\", \"message\": \"권한이 없습니다. 회원가입을 진행해주세요.\"}";
        response.getWriter().write(jsonResponse);

    }
}
