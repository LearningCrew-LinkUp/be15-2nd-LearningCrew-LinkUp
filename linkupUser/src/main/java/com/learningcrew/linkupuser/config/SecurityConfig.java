package com.learningcrew.linkupuser.config;

import com.learningcrew.linkupuser.security.filter.HeaderAuthenticationFilter;
import com.learningcrew.linkupuser.security.handler.RestAccessDeniedHandler;
import com.learningcrew.linkupuser.security.handler.RestAuthenticationEntryPoint;
import com.learningcrew.linkupuser.security.jwt.JwtTokenProvider;
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
import org.springframework.security.core.userdetails.UserDetailsService;
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
                    businessEndpoints(auths);

                    auths.anyRequest().authenticated();
                })
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
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
                "/register/success",
                "/sse/connect/**",
                "/test/alert"
        ).permitAll();
    }

    /* 사용자 접근 api */
    private void userEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/users/me",
                "/users/me/profile",
                "/users/me/manner-temperature",
                "/users/me/posts",
                "/users/me/comments",
                "/users/me/meetings/participated",
                "/users/me/point",
                "/accounts/**",
                "/auth/logout",
                "/users/withdraw",
                "users/me/mypage"
        ).hasAuthority("USER");

        auths.requestMatchers(
                "/friends/**",
                "/payments/**",
                "/members/**",
                "/user/**",
                "/businesses/me"
        ).hasAuthority("USER");

        auths.requestMatchers(HttpMethod.POST, "/businesses").hasAuthority("USER");
    }

    /* 사업자 api */
    private void businessEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                "/users/me/mypage/business"
        ).hasAuthority("BUSINESS");

        auths.requestMatchers(
                HttpMethod.PUT, "/businesses"
        ).hasAuthority("BUSINESS");
    }

    /* 관리자 api */
    private void adminEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {

        auths.requestMatchers(
                "/admin/**"
        ).hasAuthority("ADMIN");

    }


    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter(){
        return new HeaderAuthenticationFilter();
    }

}