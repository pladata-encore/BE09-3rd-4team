package com.smile.gatewayservice.filter;

import com.smile.gatewayservice.jwt.GatewayJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final GatewayJwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange); // 비인증 요청 허용
        }

        String token = authHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String id = jwtTokenProvider.getIdFromJWT(token);
        String role = jwtTokenProvider.getRoleFromJWT(token);
        String gender = jwtTokenProvider.getGenderFromJWT(token);
        Integer age = jwtTokenProvider.getAgeFromJWT(token);
        System.out.println("📦 Gateway 인증 필터 동작: id=" + id + ", gender=" + gender + ", age=" + age);

        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                .header("X-User-Id", id)
                .header("X-User-Role", role);

        if (gender != null) {
            requestBuilder.header("X-User-Gender", gender);
        }
        if (age != null) {
            requestBuilder.header("X-User-Age", String.valueOf(age));
        }

        ServerHttpRequest mutateRequest = requestBuilder.build();


        ServerWebExchange mutatedExchange = exchange.mutate().request(mutateRequest).build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
