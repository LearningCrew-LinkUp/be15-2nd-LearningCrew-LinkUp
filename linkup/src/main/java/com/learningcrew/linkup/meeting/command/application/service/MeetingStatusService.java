package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;

public interface MeetingStatusService {
    void changeStatusByMemberCount(Meeting savedMeeting);
}
