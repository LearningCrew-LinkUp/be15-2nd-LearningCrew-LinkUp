package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.MeetingParticipationCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.MeetingParticipationCommandService;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.point.command.application.dto.response.MeetingPaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "모임 참가 관리", description = "모임 참가 신청 및 취소 API")
public class MeetingParticipationController {

    private static final int STATUS_ACCEPTED = 2;

    private final MeetingParticipationCommandService meetingParticipationCommandService;
    private final MeetingRepository meetingRepository;

    @Operation(summary = "모임 참가 신청 가능 여부 확인", description = "포인트 기준 참가 가능 여부 확인")
    @GetMapping("/meetings/{meetingId}/participation/check")
    public ResponseEntity<ApiResponse<MeetingPaymentResponse>> checkEligibility(
            @PathVariable int meetingId,
            @RequestParam("userId") int userId
    ) {
        // 포인트 잔액 확인
        meetingParticipationCommandService.validateBalance(meetingId, userId);

        MeetingPaymentResponse response = MeetingPaymentResponse.builder()
                .message("참가 가능합니다.")
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "모임 참가 신청", description = "회원이 모임에 참가 신청")
    @PostMapping("/meetings/{meetingId}/participation")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> createMeetingParticipation(
            @RequestBody @Validated MeetingParticipationCreateRequest request,
            @PathVariable int meetingId
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        // 포인트 잔액 확인
        meetingParticipationCommandService.validateBalance(meetingId, request.getMemberId());

        // 참가 신청
        long participationId = meetingParticipationCommandService.createMeetingParticipation(request, meeting);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MeetingParticipationCommandResponse.builder()
                        .participationId(participationId)
                        .build()));
    }

    @Operation(summary = "모임 참가 취소", description = "회원이 모임 참가 취소")
    @DeleteMapping("/meetings/{meetingId}/participation/{memberId}")
    public ResponseEntity<ApiResponse<MeetingParticipationCommandResponse>> deleteMeetingParticipation(
            @PathVariable int meetingId,
            @PathVariable int memberId,
            @RequestParam int requesterId
    ) {
        // soft delete 수행
        long participationId = meetingParticipationCommandService.deleteMeetingParticipation(meetingId, memberId, requesterId);

        return ResponseEntity.ok(ApiResponse.success(MeetingParticipationCommandResponse.builder()
                .participationId(participationId)
                .build()));
    }


}
