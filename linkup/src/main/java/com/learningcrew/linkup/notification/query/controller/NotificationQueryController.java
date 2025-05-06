package com.learningcrew.linkup.notification.query.controller;

import com.learningcrew.linkup.notification.query.dto.response.NotificationResponse;
import com.learningcrew.linkup.notification.query.service.NotificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Notification 조회 API", description = "알림 조회 관련 엔드포인트")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationQueryService notificationQueryService;
    
    /**
     * 특정 사용자의 최신 알림 목록을 조회합니다.
     * 최근 5건의 알림 데이터를 반환합니다.
     */
    @Operation(summary = "최근 알림 조회", description = "특정 유저의 최신 알림 목록을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>> getRecentNotifications(
            @Parameter(description = "조회할 유저 ID") @PathVariable Integer userId) {
        List<NotificationResponse> notifications = notificationQueryService.getRecentNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
}