package com.learningcrew.linkupuser.query.controller;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.query.MeetingMemberDto;
import com.learningcrew.linkupuser.query.dto.response.UserStatusResponse;
import com.learningcrew.linkupuser.query.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "유저 조회", description = "유저의 프로필, 게시글, 댓글, 모임, 포인트, 친구 관련 이력을 조회합니다.")
public class UserInternalQueryController {
    private final UserQueryService userQueryService;

    @GetMapping("/me/meetings/{memberId}")
    public ResponseEntity<ApiResponse<MeetingMemberDto>> getMemberById(@PathVariable int memberId) {
        log.info("모임 조회 memberId: {}", memberId);
        return ResponseEntity.ok(ApiResponse.success(userQueryService.getMeetingMember(memberId)));
    }

    @GetMapping("/{userId}/exists")
    public Boolean existsUser(@PathVariable int userId) {
        return userQueryService.getExistsUser(userId);
    }


    @GetMapping("/me/{userId}/email")
    public String getEmailByUserId(@PathVariable int userId, HttpServletRequest request) {
        log.info("요청 URI: {}", request.getRequestURI());
        log.info("받은 userId: {}", userId);
        return userQueryService.getUserEmail(userId);
    }

    @GetMapping("/me/{userId}/userName")
    public String getUserNameByUserId(@PathVariable int userId) {
        return userQueryService.getUserNameByUserId(userId);
    }

    @GetMapping("/me/{userId}/point")
    public int getPointBalance(@PathVariable int userId) {
        return userQueryService.getPointBalance(userId);
    }

    @GetMapping("/me/{userId}/status")
    @Operation(summary = "유저 상태 조회", description = "유저의 현재 상태 (ACTIVE, INACTIVE, BLACKLISTED)를 조회합니다.")
    public ResponseEntity<ApiResponse<UserStatusResponse>> getUserStatus(@PathVariable int userId) {
        UserStatusResponse response = userQueryService.getUserStatus(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "유저 상태 조회 성공"));
    }


}
