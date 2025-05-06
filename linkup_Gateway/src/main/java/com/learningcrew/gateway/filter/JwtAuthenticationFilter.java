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
//
//    private final GatewayJwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return chain.filter(exchange);
//        }
//
//        String token = authHeader.substring(7);
//
//        jwtTokenProvider.validateToken(token);
//
//        // 토큰에서 사용자 정보 추출
//        String userId = jwtTokenProvider.getUserIdFromJwt(token);
//        String role = jwtTokenProvider.getRoleFromJWT(token);
//
//        ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
//                .header("X-User-Id", userId)
//                .header("X-User-Role", role)
//                .build();
//
//        ServerWebExchange mutatedExchange = exchange.mutate().request(mutateRequest).build();
//
//        return chain.filter(mutatedExchange);
//    }
//
//    /* GlobalFilter (전역 필터) 의 우선 순위를 지정한다.
//    * 숫자가 작을 수록 높은 우선 순위를 가진다. */
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}

    private final GatewayJwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        String token = null;

        // ✅ 디버그 로그 추가 (필요 시)
        System.out.println("💡 요청 URI path = " + path);

        // ✅ 1. SSE 요청 → 쿼리 파라미터에서 토큰 추출
        if (path.contains("/sse/connect")) { // 변경: startsWith → contains
            String tokenParam = request.getQueryParams().getFirst("token");
            if (tokenParam != null && tokenParam.startsWith("Bearer ")) {
                token = tokenParam.substring(7); // "Bearer " 접두어 제거
            }
        }

        // ✅ 2. 일반 요청 → Authorization 헤더에서 토큰 추출
        if (token == null) {
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        // ✅ 3. 토큰이 없다면 인증 없이 통과
        if (token == null) {
            return chain.filter(exchange);
        }

        // ✅ 4. 토큰 검증 및 사용자 정보 추출
        jwtTokenProvider.validateToken(token);
        String userId = jwtTokenProvider.getUserIdFromJwt(token);
        String role = jwtTokenProvider.getRoleFromJWT(token);

        // ✅ 5. 사용자 정보를 헤더에 추가해서 내부 서비스로 전달
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}