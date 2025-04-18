package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.ParticipantReview;
import com.learningcrew.linkup.meeting.command.domain.repository.ParticipantReviewRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaParticipantReviewRepository extends ParticipantReviewRepository, JpaRepository<ParticipantReview, Long> {

    boolean existsByMeetingIdAndReviewerIdAndRevieweeId(int meetingId, int reviewerId, int revieweeId);

}
