package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.request.ReviewSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewListResponse;
import com.learningcrew.linkup.meeting.query.service.AdminMeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자 모임 조회", description = "관리자가 전체 모임 이력 및 평가 내역을 조회하는 API")
public class AdminMeetingController {

    private final AdminMeetingService adminMeetingService;

    @GetMapping("/meetings/list")
    @Operation(summary = "전체 모임 내역 조회", description = "관리자가 서비스에 등록된 모든 모임 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getAllMeetings(@ModelAttribute MeetingSearchRequest request) {
        request.applyDateDefaults("ADMIN_HISTORY");
        return ResponseEntity.ok(ApiResponse.success(adminMeetingService.getAllMeetings(request)));
    }

    @GetMapping("/meetings/review")
    @Operation(summary = "참가자 평가 조회", description = "관리자가 서비스에서 작성된 모든 참가자 평가를 조건에 따라 조회하는 기능")
    public ResponseEntity<ApiResponse<ParticipantReviewListResponse>> getParticipantReviews(@ModelAttribute ReviewSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(adminMeetingService.getParticipantReviews(request)));
    }

}
