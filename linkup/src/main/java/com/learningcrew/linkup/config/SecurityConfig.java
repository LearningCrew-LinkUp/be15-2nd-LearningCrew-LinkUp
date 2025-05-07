package com.learningcrew.linkup.config;

import com.learningcrew.linkup.security.filter.HeaderAuthenticationFilter;
import com.learningcrew.linkup.security.handler.RestAccessDeniedHandler;
import com.learningcrew.linkup.security.handler.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // 비밀번호 암호화 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 스프링 시큐리티 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화 (JWT 기반)
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(restAuthenticationEntryPoint) // 인증 실패 처리
                                .accessDeniedHandler(restAccessDeniedHandler) // 인가 실패 처리
                )
                .authorizeHttpRequests(auths -> {
                    permitAllEndpoints(auths);       // 인증 없이 접근 허용되는 경로
                    userEndpoints(auths);            // 사용자 권한 전용 경로
                    adminEndpoints(auths);           // 관리자 전용 경로
                    businessEndpoints(auths);        // 사업자 전용 경로
                    sharedAuthEndpoints(auths);      // 복수 권한 접근 허용 경로

                    auths.anyRequest().authenticated(); // 이 외 요청은 인증 필요
                })
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 등록

        return http.build();
    }

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter() {
        return new HeaderAuthenticationFilter();
    }

    // 인증 없이 접근 가능한 공개 API 목록
    private void permitAllEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/users/register",
                "/auth/login",
                "/auth/password/reset-link",
                "/auth/password/reset",
                "/users/recover",
                "/auth/refresh",
                "/auth/verify-email",
                "/payments/me/history",
                "/places",
                "/place/{placeId}",
                "/meetings",                    // 모임 목록 조회
                "/posts",                         // 커뮤니티 게시글 목록 조회
                "/report/types"
        ).permitAll();
    }

    // 일반 사용자 권한 전용 API
    private void userEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/auth/**",
                "/friends/**",
                "/payments/**",
                "/owners/**",
                "/members/**",
                "/user/**",
                "/businesses/**",
                "/meetings/user/{userId}/created",
                "/meetings/user/{userId}/pending",
                "/meetings/{meetingId}/participation_request",
                "/meetings/{meetingId}/participation/{memberId}/accept",
                "/meetings/{meetingId}/participation/{memberId}/reject",
                "/my/meetings/{meetingId}/participation",
                "/meetings/{meetingId}/change-leader/{memberId}",
                "/meetings/{meetingId}/cancel",
                "/meetings/{meetingId}/participation",
                "/meetings/{meetingId}/participation/{memberId}",
                "/meetings/interested/{userId}",
                "/members/{memberId}/interested-meetings",
                "/members/{memberId}/interested-meetings/{meetingId}",
                "/meetings/{meetingId}/review",
                "/user/{userId}/favorite",
                "/notification/{userId}",
                "/notification/{userId}/setting",
                "/notification/{notificationId}",
                "/objections/post/{postId}",
                "/objections/comment/{commentId}"
        ).hasAuthority("USER");
    }

    // 사업자 권한 전용 API
    private void businessEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(HttpMethod.PUT, "/businesses").hasAuthority("BUSINESS");

        auths.requestMatchers(
                "/place",
                "/place/{id}",
                "/place/{id}/times",
                "/place/{id}/images",
                "/notification/{userId}",
                "/notification/{userId}/setting",
                "/notification/{notificationId}"
        ).hasAuthority("BUSINESS");
    }

    // 관리자 권한 전용 API
    private void adminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/admin/users/point/transaction",
                "/admin/businesses",

                // 모임 관리
                "/meetings",
                "/meetings/list",
                "/meetings/review",
                "/my/meetings/{meetingId}/participation",
                "/meetings/{meetingId}/participation",

                // 장소 및 예약 관리
                "/admin/places",
                "/owner/{ownerId}/places",
                "/owner/{ownerId}/reserve",

                "/users/accounts",
                "/settlements/users",

                // 커뮤니티 관리
                "/posts/list",
                "/posts/user/{userId}",
                "/posts/{postId}/delete",
                "/comments",
                "/comments/user/{userId}",

                // 신고 관리
                "/report",
                "/report/{reportId}",
                "/report/target",
                "/report/target/{targetType}/{targetId}",
                "/report/reporter-user",
                "/report/reportee-user",
                "/report/reportee-user/{reportedId}",
                "/report/reporter-user/{reporteeId}",
                "/report/user",
                "/report/post",
                "/report/comment",
                "/report/{reportId}/rejected",
                "/report/{reportId}/accepted",

                // 제재 및 이의제기
                "/penalty",
                "/penalty{penaltyId}",
                "/penalty/post/{postId}",
                "/penalty/comment/{commentId}",
                "/penalty/placeReview/{reviewId}/done",
                "/penalty/{penaltyId}",
                "/objections",
                "/objections/{objectionId}",
                "/objections/{objectionId}/accept",
                "/objections/{objectionId}/reject",

                // 블랙리스트 관리
                "/blacklist",
                "/blacklist/{memberId}",
                "/blacklist/{memberId}/clear"
        ).hasAuthority("ADMIN");

        // 관리자 인증 심사
        auths.requestMatchers(HttpMethod.GET, "/admin/businesses/pending").hasAuthority("ADMIN");
        auths.requestMatchers(HttpMethod.PUT, "/admin/businesses/{targetId}/approve", "/admin/businesses/{targetId}/reject").hasAuthority("ADMIN");
    }

    // 다중 권한 접근 허용 API
    private void sharedAuthEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        // 사용자 + 관리자
        auths.requestMatchers(
                "/users/**",
                "/meetings",
                "/meetings/{meetingId}",
                "/meetings/{meetingId}/participation",
                "/meetings/user/{userId}",
                "/meetings/user/{userId}/done",
                "/posts/search/{keyword}",
                "/posts/{postId}",
                "/posts/{postId}/delete",
                "/posts/{postId}/comments",
                "/posts/{postId}/comments/{commentId}/delete",
                "/posts/{postId}/likes",
                "/comments/{commentId}/likes",
                "/penalty/user/{memberId}",
                "/objections/user/{memberId}"
        ).hasAnyAuthority("USER", "ADMIN");

        // 사업자 + 관리자
        auths.requestMatchers(
                "/owner/{ownerId}/places",
                "/owner/{ownerId}/reserve"
        ).hasAnyAuthority("BUSINESS", "ADMIN");
    }
}
