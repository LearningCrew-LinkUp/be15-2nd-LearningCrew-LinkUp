package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.ParticipantReviewCreateRequest;

public interface ParticipantReviewCommandService {

    long createParticipantReview(ParticipantReviewCreateRequest request, int revieweeId, int reviewerId, int meetingId);
}
