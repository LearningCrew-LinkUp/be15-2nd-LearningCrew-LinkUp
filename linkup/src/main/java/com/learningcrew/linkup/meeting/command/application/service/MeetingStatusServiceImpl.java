package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingStatusServiceImpl implements MeetingStatusService {
    private final MeetingRepository meetingRepository;
    private final MeetingParticipationHistoryRepository meetingParticipationHistoryRepository;

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_ACCEPTED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_DELETED = 4;
    private static final int STATUS_DONE = 5;

    public void changeStatusByMemberCount(Meeting meeting) { // 정합성 체크
        meeting = meetingRepository.findById(meeting.getMeetingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEETING_NOT_FOUND));

        List<MeetingParticipationHistory> meetingParticipationHistories
                = meetingParticipationHistoryRepository.findAllByMeetingIdAndStatusId(meeting.getMeetingId(), STATUS_ACCEPTED);

        int participantsCount = meetingParticipationHistories.size();

        if (participantsCount < meeting.getMinUser()) {
            meeting.setStatusId(STATUS_PENDING);
        }
        if (participantsCount >= meeting.getMinUser() && participantsCount < meeting.getMaxUser()) {
            meeting.setStatusId(STATUS_ACCEPTED);
        }
        if (participantsCount == meeting.getMaxUser()) {
            meeting.setStatusId(STATUS_REJECTED);
        }

        meetingRepository.save(meeting);
    }
}
