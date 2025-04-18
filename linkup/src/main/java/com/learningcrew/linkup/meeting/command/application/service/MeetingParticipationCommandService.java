package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingParticipationCreateRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;

public interface MeetingParticipationCommandService {
    void validateBalance(int meetingId, int memberId);

    long acceptParticipation(Meeting meeting, int memberId);

    long createMeetingParticipation(MeetingParticipationCreateRequest request, Meeting meeting);

    void cancelParticipation(int meetingId, int memberId);

    long rejectParticipation(Meeting meeting, int memberId);

    long deleteMeetingParticipation(MeetingParticipationHistory participation);
}
