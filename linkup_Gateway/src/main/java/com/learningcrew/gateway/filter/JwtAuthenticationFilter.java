package com.learningcrew.gateway.filter;

import com.learningcrew.gateway.jwt.GatewayJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
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

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        jwtTokenProvider.validateToken(token);

        // 토큰에서 사용자 정보 추출
        String userId = jwtTokenProvider.getUserIdFromJwt(token);
        String role = jwtTokenProvider.getRoleFromJWT(token);

        ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutateRequest).build();

        return chain.filter(mutatedExchange);
    }

    /* GlobalFilter (전역 필터) 의 우선 순위를 지정한다.
    * 숫자가 작을 수록 높은 우선 순위를 가진다. */
    @Override
    public int getOrder() {
        return -1;
    }
}
