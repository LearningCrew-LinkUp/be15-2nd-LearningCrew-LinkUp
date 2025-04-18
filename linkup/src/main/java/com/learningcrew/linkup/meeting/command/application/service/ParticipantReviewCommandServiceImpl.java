package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.application.dto.request.ParticipantReviewCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.aggregate.ParticipantReview;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.ParticipantReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantReviewCommandServiceImpl implements ParticipantReviewCommandService {

    private static final int REVIEW_DUE_DAY = 1;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 5;
    private static final int STATUS_DONE = 5;

    private final ParticipantReviewRepository reviewRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository participationRepository;

    @Transactional
    public long createParticipantReview(ParticipantReviewCreateRequest request, int revieweeId, int reviewerId, int meetingId) {

        // 1. 모임 존재 여부 (아예 3번과 합쳐서 status 5를 받아와도 될 듯)
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        // 2. 참가 완료된 사람들만 리뷰 가능 (STATUS_DONE = 5)
        List<MeetingParticipationHistory> doneParticipants =
                participationRepository.findByMeetingIdAndStatusId(meetingId, STATUS_DONE);

        List<Integer> participantIds = doneParticipants.stream()
                .map(MeetingParticipationHistory::getMemberId)
                .toList();

        if (!participantIds.contains(reviewerId) || !participantIds.contains(revieweeId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "리뷰 대상이 모임에 참여하지 않았습니다.");
        }

        // 3. 모임이 종료 상태인지 확인
        if (meeting.getStatusId() != STATUS_DONE) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "모임이 종료되지 않았습니다.");
        }

        // 4. 작성 기한 확인
        LocalDateTime deadline = meeting.getDate().plusDays(REVIEW_DUE_DAY).atTime(meeting.getEndTime());
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_EXISTS, "평가 가능 기간이 지났습니다.");
        }

        // 5. 자기 자신은 평가 불가
        if (reviewerId == revieweeId) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "자기 자신은 평가할 수 없습니다.");
        }

        // 6. 중복 리뷰 확인
        boolean isAlreadyReviewed = reviewRepository.existsByMeetingIdAndReviewerIdAndRevieweeId(
                meetingId, reviewerId, revieweeId);
        if (isAlreadyReviewed) {
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // 7. 점수 유효성
        int score = request.getScore();
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new BusinessException(ErrorCode.INVALID_REVIEW_SCORE);
        }

        // 8. 저장
        ParticipantReview review = ParticipantReview.builder()
                .meetingId(meetingId)
                .reviewerId(reviewerId)
                .revieweeId(revieweeId)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(review).getReviewId();
    }

}
