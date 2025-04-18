package com.learningcrew.linkup.meeting.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.command.application.dto.request.ParticipantReviewCreateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.response.ParticipantReviewCommandResponse;
import com.learningcrew.linkup.meeting.command.application.service.ParticipantReviewCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Tag(name = "참가자 평가", description = "참가자 평가 API")
public class ParticipantReviewController {

    private final ParticipantReviewCommandService service;

    @Operation(
            summary = "참가자 평가",
            description = "회원이 진행 완료된 모임에 대하여 모임 참가자를 평가한다."
    )
    @PostMapping("/meetings/{meetingId}/review/{revieweeId}")
    public ResponseEntity<ApiResponse<ParticipantReviewCommandResponse>> createParticipantReview(
            @RequestBody @Validated ParticipantReviewCreateRequest request,
            @PathVariable int meetingId, @PathVariable int revieweeId
    ) {
        int reviewerId = request.getReviewerId();

        long reviewId = service.createParticipantReview(request, revieweeId, reviewerId, meetingId);

        ParticipantReviewCommandResponse response
                = ParticipantReviewCommandResponse
                .builder()
                .reviewId(reviewId)
                .revieweeId(revieweeId)
                .meetingId(meetingId)
                .score(request.getScore())
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

}
