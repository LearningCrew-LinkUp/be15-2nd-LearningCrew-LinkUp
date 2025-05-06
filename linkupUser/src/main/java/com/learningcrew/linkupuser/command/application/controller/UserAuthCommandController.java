package com.learningcrew.linkupuser.command.application.controller;


import com.learningcrew.linkupuser.command.application.dto.request.FindPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.request.LoginRequest;
import com.learningcrew.linkupuser.command.application.dto.request.RefreshTokenRequest;
import com.learningcrew.linkupuser.command.application.dto.request.ResetPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.response.TokenResponse;
import com.learningcrew.linkupuser.command.application.service.AuthCommandService;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.learningcrew.linkupuser.command.application.utility.CookieUtils.createDeleteRefreshTokenCookie;
import static com.learningcrew.linkupuser.command.application.utility.CookieUtils.createRefreshTokenCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "회원 인증", description = " 로그인, 인증 관련 API")
public class UserAuthCommandController {
    private final AuthCommandService userAuthCommandService;

    /* 자체 로그인 */
    @Operation(summary = "로그인", description = "자체 로그인 후 JWT를 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request){
        log.info("로그인 요청: email={}", request.getEmail());
        TokenResponse token = userAuthCommandService.login(request);
        log.info("로그인 성공: accessToken 발급 완료");
        return buildTokenResponse(token);
    }


    /* 이메일 인증 */
    @Operation(summary = "이메일 인증")
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam("token") String token) {
        log.info("이메일 인증 요청: token={}", token);
        userAuthCommandService.verifyEmail(token);
        log.info("이메일 인증 성공");
        // 리다이렉트 주소를 헤더로 반환
        return ResponseEntity.ok(ApiResponse.success(null, "이메일 인증에 성공했습니다"));
    }

    /* 토큰 재발급 */
    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> getAccessTokenByRefreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken // HttpOnly 쿠키에서 읽어옴
    ){
        log.info("토큰 재발급 요청: refreshToken={}", refreshToken);
        if(refreshToken == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 에러 반환
        }
        TokenResponse token = userAuthCommandService.refreshToken(refreshToken);
        log.info("토큰 재발급 성공: accessToken 및 refreshToken 갱신 완료");
        return buildTokenResponse(token);
    }

    /* 로그아웃 */
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken // HttpOnly 쿠키에서 읽어옴
    ){
        if (refreshToken != null) {
            userAuthCommandService.logout(refreshToken);
        }
        log.info("로그아웃 처리 완료: refreshToken 삭제됨");
        ResponseCookie deleteCookie = createDeleteRefreshTokenCookie(); // 만료용 쿠키 생성
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    /* 비밀번호 찾기 - 비밀번호 재설정 url 발송 */
    @PostMapping("/password/reset-link")
    @Operation(summary = "비밀번호 재설정 URL 발송", description = "이메일 조회 후 재설정 url을 이메일로 발송")
    public ResponseEntity<ApiResponse<Void>> sendPasswordResetUrl(@Valid @RequestBody FindPasswordRequest request) {
        userAuthCommandService.sendPasswordResetLink(request);
        return ResponseEntity.ok(ApiResponse.success(null, "비밀번호 재설정 메일을 전송했습니다."));
    }

    /* 비밀번호 재설정 */
    @Operation(summary = "비밀번호 재설정")
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("비밀번호 재설정 요청: email={}, token={}, password={}", request.getEmail(), request.getToken(), request.getNewPassword());
        userAuthCommandService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "비밀번호가 변경되었습니다."));
    }

    /* accessToken 과 refreshToken을 body와 쿠키에 담아 반환 */
    private ResponseEntity<ApiResponse<TokenResponse>> buildTokenResponse(TokenResponse tokenResponse) {
        ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());  // refreshToken 쿠키 생성
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(tokenResponse));
    }

}
