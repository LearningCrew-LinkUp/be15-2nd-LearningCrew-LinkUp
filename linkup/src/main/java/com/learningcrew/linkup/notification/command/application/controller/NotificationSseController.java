package com.learningcrew.linkup.notification.command.application.controller;

import com.learningcrew.linkup.notification.command.infrastructure.NotificationSseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

//@Tag(name = "SSE 연결 API", description = "실시간 알림 수신을 위한 SSE 엔드포인트")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/sse")
//public class NotificationSseController {
//
//    private final NotificationSseService notificationSseService;
//
//    /**
//     * 클라이언트가 SSE 연결을 요청하는 엔드포인트
//     * @param userId 연결할 사용자 ID
//     * @return SseEmitter (지속 연결)
//     */
//    @Operation(summary = "SSE 연결", description = "유저 ID로 실시간 알림을 구독합니다.")
//    @GetMapping("/connect/{userId}")
//    public SseEmitter connect(
//            @Parameter(description = "SSE 연결할 유저 ID") @PathVariable Integer userId) {
//        return notificationSseService.connect(userId);
//    }
//}
@Tag(name = "SSE 연결 API", description = "실시간 알림 수신을 위한 SSE 엔드포인트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class NotificationSseController {

    private final NotificationSseService notificationSseService;

    /**
     * 클라이언트가 SSE 연결을 요청하는 엔드포인트
     * @param userId 연결할 사용자 ID
     * @param authenticatedUserId 게이트웨이에서 전달된 인증된 사용자 ID (X-User-Id 헤더)
     * @return SseEmitter (지속 연결)
     */
    @Operation(summary = "SSE 연결", description = "유저 ID로 실시간 알림을 구독합니다.")
    @GetMapping("/connect/{userId}")
    public SseEmitter connect(
            @Parameter(description = "SSE 연결할 유저 ID") @PathVariable Integer userId,
            @RequestHeader("X-User-Id") Integer authenticatedUserId) {

        // 인증된 사용자와 요청된 userId가 일치하는지 확인
        if (!userId.equals(authenticatedUserId)) {
            throw new SecurityException("인증된 사용자 정보와 요청 정보가 일치하지 않습니다.");
        }

        return notificationSseService.connect(userId);
    }
}