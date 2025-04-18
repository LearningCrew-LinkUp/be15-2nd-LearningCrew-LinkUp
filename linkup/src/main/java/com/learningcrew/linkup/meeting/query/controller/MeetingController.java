package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDetailResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantsResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "모임 조회", description = "모임 관련 조회 및 정보 API")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping("/meetings")
    @Operation(summary = "모임 목록 조회", description = "현재 모집 중이거나 예정된 모임 목록을 조회합니다. 필터 조건을 사용하여 검색할 수 있습니다.")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getMeetings(@ModelAttribute MeetingSearchRequest request) {
        request.applyDateDefaults("USER_SEARCH");
        return ResponseEntity.ok(ApiResponse.success(meetingService.getMeetings(request)));
    }

    @GetMapping("/meetings/{meetingId}")
    @Operation(summary = "모임 상세 조회", description = "특정 모임의 상세 정보를 조회합니다. 모집 상태가 승인(ACCEPTED)일 경우, 참가자 목록도 함께 조회됩니다.")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> getMeetingDetail(@PathVariable int meetingId) {
        return ResponseEntity.ok(ApiResponse.success(meetingService.getMeetingDetail(meetingId)));
    }

    @GetMapping("/meetings/user/{userId}")
    @Operation(summary = "회원별 승인된 참가 모임 목록 조회", description = "회원이 참가 신청 후 승인된 모임 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getAcceptedMeetingsByUser(@PathVariable int userId) {
        return ResponseEntity.ok(ApiResponse.success(meetingService.getAcceptedMeetingsByUser(userId)));
    }

    @GetMapping("/meetings/user/{userId}/done")
    @Operation(summary = "회원별 과거 모임 이력 조회", description = "해당 회원이 참가했던 과거 모임 목록을 조회합니다. (모임 날짜가 오늘 이전인 경우)")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getPastMeetingsByUser(@PathVariable int userId) {
        return ResponseEntity.ok(ApiResponse.success(meetingService.getPastMeetingsByUser(userId)));
    }

    @GetMapping("/meetings/interested/{userId}")
    @Operation(summary = "찜한 모임 목록 조회", description = "회원이 관심 등록(찜)한 모임 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<MeetingListResponse>> getInterestedMeetings(@PathVariable int userId) {
        return ResponseEntity.ok(ApiResponse.success(meetingService.getInterestedMeetings(userId)));
    }

    @Operation(
            summary = "모임 참가 요청 목록 조회",
            description = "개설자가 자신이 만든 모임에 참가 요청한 회원 목록을 확인한다."
    )
    @GetMapping("/meetings/{meetingId}/participation_request")
    public ResponseEntity<ApiResponse<ParticipantsResponse>> getParticipationRequests(
            @PathVariable int meetingId,
            @Parameter(description = "요청자(개설자) ID", required = true)
            @RequestParam int requesterId // 일반적으로 인증정보에서 가져오나, 예시로 RequestParam 사용
    ) {
        List<MemberDTO> pendingParticipants = meetingService.getPendingParticipantsByMeetingId(meetingId, requesterId);
        ParticipantsResponse response = ParticipantsResponse.from(pendingParticipants);
        return ResponseEntity.ok(ApiResponse.success(response));
    }





}
