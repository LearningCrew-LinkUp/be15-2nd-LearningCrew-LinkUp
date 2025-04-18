package com.learningcrew.linkup.place.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
import com.learningcrew.linkup.place.command.application.dto.request.PlaceReviewCreateRequest;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.PlaceReview;
import com.learningcrew.linkup.place.command.domain.repository.PlaceReviewRepository;
import com.learningcrew.linkup.place.query.dto.response.PlaceReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceReviewCommandService {
    private final JpaMeetingParticipationHistoryRepository participationRepository;
    private final MeetingRepository meetingRepository;
    private final PlaceReviewRepository placeReviewRepository;


    private static final int STATUS_REVIEW_WRITTEN = 2;

    public PlaceReviewResponse createReview(PlaceReviewCreateRequest request) {
        List<MeetingParticipationHistory> participationList =
                participationRepository.findByMemberIdAndMeetingIdAndStatusId(
                        request.getMemberId(), request.getMeetingId(), STATUS_REVIEW_WRITTEN
                );

        if (participationList.isEmpty()) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "해당 모임에서 유효한 참여 기록이 없습니다.");
        }

        MeetingParticipationHistory participation = participationList.get(0);

        Meeting meeting = meetingRepository.findById(request.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        PlaceReview review = PlaceReview.builder()
                .memberId(request.getMemberId())
                .participationId(participation.getParticipationId())
                .placeId(meeting.getPlaceId())
                .statusId(STATUS_REVIEW_WRITTEN)
                .reviewContent(request.getReviewContent())
                .reviewImageUrl(request.getReviewImageUrl())
                .reviewScore(request.getReviewScore())
                .build();

        placeReviewRepository.save(review);

        return new PlaceReviewResponse(
                review.getReviewContent(),
                review.getReviewImageUrl(),
                review.getReviewScore()
        );
    }
}