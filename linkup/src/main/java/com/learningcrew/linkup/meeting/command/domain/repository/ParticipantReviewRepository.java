package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.ParticipantReview;

import java.util.List;

public interface ParticipantReviewRepository {
    ParticipantReview save(ParticipantReview review);
    boolean existsByMeetingIdAndReviewerIdAndRevieweeId(int meetingId, int reviewerId, int revieweeId);

    List<ParticipantReview> findAllByMeetingId(int meetingId);
}
