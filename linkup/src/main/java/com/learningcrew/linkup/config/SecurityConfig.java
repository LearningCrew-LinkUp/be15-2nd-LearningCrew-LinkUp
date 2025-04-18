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
@EnableMethodSecurity //컨트롤 메서드 제어 가능하도록 활성화
@EnableWebSecurity
public class SecurityConfig {
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(restAuthenticationEntryPoint)     // 인증 실패
                                .accessDeniedHandler(restAccessDeniedHandler)          // 인가 실패
                )
                .authorizeHttpRequests(auths -> {
                    permitAllEndpoints(auths);
                    userEndpoints(auths);
                    adminEndpoints(auths);
                    sharedAuthEndpoints(auths);
                    businessEndpoints(auths);

                    auths.anyRequest().authenticated();
                })
                // 커스텀 인증 필터(jwt 토큰 필터)
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter(){
        return new HeaderAuthenticationFilter();
    }

    /* 관리자 api */
    private void adminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/report",
                "/report/reportType/{reportTypeId}",
                "/report/reporter-user",
                "/report/reportee-user",
                "/report/reporter-user/{reportedId}",
                "/report/reportee-user/{reporteeId}",
                "/report/{reportId}/rejected",
                "/report/{reportId}/accepted",
                "/penalty",
                "/penalty/{penaltyType}",
                "/penalty/user/{memberId}",
                "/penalty/user/{memberId}/{penaltyType}",
                "/penalty/post/{postId}",
                "/penalty/comment/{commentId}",
                "/penalty/placeReview/{reviewId}",
                "/penalty/placerRview/{reviewId}/done",
                "/penalty/{penaltyId}",
                "/objections",
                "/objections/status/{statusId}",
                "/objections/user/{memberId}",
                "/objections/{objectionId}/accept",
                "/objections/{objectionId}/reject",
                "/blacklist",
                "/blacklist/{memberId}",
                "/blacklist/{memberId}/clear",
                "/posts/list",
                "/post/user/{userId}",
                "/admin/users",
                "/meetings/list",
                "/meetings/review",
                "/meetings/review/reviewer/{memberId}",
                "/meetings/review/reviewee/{memberId}",
                "/admin/places"
        ).hasAuthority("ADMIN");

        auths.requestMatchers(
                HttpMethod.GET, "/admin/businesses/pending"
        ).hasAuthority("ADMIN");

        auths.requestMatchers(HttpMethod.PUT,
                "/admin/businesses/{targetId}/approve",
                "/admin/businesses/{targetId}/reject"
        ).hasAuthority("ADMIN");
    }

    /* 사업자 api */
    private void businessEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                HttpMethod.PUT, "/businesses"
        ).hasAuthority("BUSINESS");

        auths.requestMatchers(
                "/place",
                "/place/:id",
                "/place/:id/times",
                "/place/:id/images",
                "/owner/{ownerId}/places"
        ).hasAuthority("BUSINESS");
    }


    /* 다중 권한 api */
    private void sharedAuthEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        //회원 OR 관리자
        auths.requestMatchers(
                "/meetings",
                "/meetings/user/{userId}",
                "/meetings/user/{userId}/done",
                "/meetings/{meetingId}/participation",
                "/posts",
                "/posts/{postId}",
                "/posts/{postId}/delete",
                "/postComment",
                "/postComment/{postCommentId}"
        ).hasAnyAuthority("ADMIN", "USER");

        //사업자 OR 관리자
        auths.requestMatchers(
                "/owner/{ownerId}/reserve"
        ).hasAnyAuthority("ADMIN", "BUSINESS");

    }

    /* 공통 api */
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
                "/payments/me/history"
        ).permitAll();
    }

    /* 사용자 접근 api */
    private void userEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/users/**",
                "/accounts/**",
                "/auth/**",
                "/friends/**",
                "/posts/**",
                "/comments/**",
                "/postComment/**",
                "/payments/**",
                "/place/**",
                "/places/**",
                "/owners/**",
                "/members/**",
                "/user/**",
                "/businesses/**",
                "/objections/**"
        ).hasAuthority("USER");
    }
}