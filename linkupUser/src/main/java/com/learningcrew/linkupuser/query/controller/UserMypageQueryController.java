package com.learningcrew.linkupuser.query.controller;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.query.*;
import com.learningcrew.linkupuser.query.dto.response.UserProfileResponse;
import com.learningcrew.linkupuser.query.service.CommunityQueryService;
import com.learningcrew.linkupuser.query.service.MeetingQueryService;
import com.learningcrew.linkupuser.query.service.PaymentQueryService;
import com.learningcrew.linkupuser.query.service.UserQueryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@Tag(name = "유저 활동 조회", description = "유저의 프로필, 게시글, 댓글, 모임, 포인트, 친구 관련 이력을 조회합니다.")
public class UserMypageQueryController {
    private final UserQueryServiceImpl userQueryService;
    private final CommunityQueryService communityQueryService;
    private final MeetingQueryService meetingQueryService;
    private final PaymentQueryService paymentQueryService;

    /* 프로필 조회 */
    @GetMapping("/profile")
    @Operation(summary = "프로필 조회", description = "사용자가 자신의 프로필 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal String userId
    ){
        log.info("프로필 조회 요청: userId={}",
               Integer.parseInt(userId));
        UserProfileResponse userProfileResponse = userQueryService.getUserProfile(Integer.parseInt(userId));
        log.info("프로필 조회 성공");
        return ResponseEntity.ok(ApiResponse.success(userProfileResponse));
    }

    /* 작성한 게시글 조회 */
    @GetMapping("/posts")
    @Operation(summary = "작성한 게시글 조회", description = "사용자가 작성한 게시글 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<UserPostDto>>> getUserPosts(@AuthenticationPrincipal String userId) {
        List<UserPostDto> posts = communityQueryService.findPostsByUser(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(posts,"게시글 조회에 성공했습니다."));
    }

    /* 작성한 댓글 조회 */
    @GetMapping("/comments")
    @Operation(summary = "작성한 댓글 조회", description = "사용자가 작성한 댓글 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<UserCommentDto>>> getUserComments(@AuthenticationPrincipal String userId) {
        List<UserCommentDto> comments = communityQueryService.findCommentsByUser(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(comments, "댓글 조회에 성공했습니다. "));
    }

    /* 참여 모임 이력 조회 */
    @GetMapping("/meetings")
    @Operation(summary = "참여한 모임 이력 조회", description = "사용자가 참여한 모임 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<UserMeetingDto>>> getUserMeetingHistory(@AuthenticationPrincipal String userId) {
        List<UserMeetingDto> meetings = meetingQueryService.findMeetingsByUser(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(meetings, "참여 모임 이력 조회에 성공했습니다. "));
    }

    /* 포인트 조회 */
    @GetMapping("/point")
    @Operation(summary = "포인트 조회", description = "사용자의 현재 포인트를 조회합니다.")
    public ResponseEntity<ApiResponse<UserPointDto>> getUserPoints(@AuthenticationPrincipal String userId) {
        log.info("포인트 조회 user : {}",userId);
        UserPointDto point = paymentQueryService.findUserPoint(Integer.parseInt(userId));
        log.info("포인트 조회에 성공했습니다.");
        return ResponseEntity.ok(ApiResponse.success(point, "포인트 조회에 성공했습니다."));
    }


    /* 매너 온도 조회 */
    @GetMapping("/manner")
    @Operation(summary = "매너온도 조회", description = "사용자의 매너온도를 조회합니다.")
    public ResponseEntity<ApiResponse<UserMannerTemperatureDto>> getMannerTemperature(@AuthenticationPrincipal String userId) {
        UserMannerTemperatureDto manner = userQueryService.getMannerTemperature(Integer.parseInt(userId));
        return ResponseEntity.ok(ApiResponse.success(manner, "매너 온도 조회에 성공했습니다. "));
    }

    /* 회원 마이페이지 조회 */

    /* 사업자 마이페이지 조회 */

}
