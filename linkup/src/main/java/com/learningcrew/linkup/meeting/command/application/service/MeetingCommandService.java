package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.LeaderUpdateRequest;
import com.learningcrew.linkup.meeting.command.application.dto.request.MeetingCreateRequest;

public interface MeetingCommandService {
    int createMeeting(MeetingCreateRequest meetingCreateRequest);

    int updateLeader(int meetingId, int memberId, LeaderUpdateRequest request);

    void deleteMeeting(int meetingId);
}
