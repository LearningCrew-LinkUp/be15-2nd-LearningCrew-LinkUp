package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.InterestedMeetingCommandRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.InterestedMeetingCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.InterestedMeetingCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name="모임 찜 관리", description = "모임 찜 등록 및 삭제 API")
public class InterestedMeetingCommandController {

    private final InterestedMeetingCommandService service;

    @Operation(
            summary = "모임 찜 등록",
            description = "회원이 관심 있는 모임을 모임 찜에 등록한다."
    )
    @PostMapping("/members/{memberId}/interested-meetings")
    public ResponseEntity<ApiResponse<InterestedMeetingCommandResponse>> createInterestedMeeting(
            @PathVariable int memberId,
            @RequestBody @Validated InterestedMeetingCommandRequest request
    ) {
        if (memberId != request.getMemberId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int meetingId = service.createInterestedMeeting(request);
        InterestedMeetingCommandResponse response
                = InterestedMeetingCommandResponse
                .builder()
                .meetingId(meetingId)
                .memberId(request.getMemberId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(
            summary = "모임 찜 취소",
            description = "회원이 모임 찜에 등록한 모임을 찜 취소한다."
    )
    @DeleteMapping("/members/{memberId}/interested-meetings")
    public ResponseEntity<ApiResponse<InterestedMeetingCommandResponse>> deleteInterestedMeeting(
            @PathVariable int memberId,
            @RequestBody @Validated InterestedMeetingCommandRequest request
    ) {
        if (memberId != request.getMemberId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        service.deleteInterestedMeeting(request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}