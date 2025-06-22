package com.smile.recommendservice.config;

import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@RequiredArgsConstructor
public class FeignAuthConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof UserDetailsWrapper userDetails) {
                    String userId = userDetails.getUser().getUserId();
                    String role = userDetails.getUser().getRole();
                    String gender = userDetails.getUser().getGender();
                    Integer age = userDetails.getUser().getAge();

                    template.header("X-User-Id", userId);
                    template.header("X-User-Role", role);
                    template.header("X-User-Gender", gender);
                    template.header("X-User-Age", String.valueOf(age));

                    System.out.println("✅ Feign에 헤더 추가: " + userDetails.getUser());
                } else {
                    System.out.println("❌ principal이 예상한 타입이 아님: " + principal);
                }
            } else {
                System.out.println("❌ 인증 정보 없음");
            }
        };
    }


}
