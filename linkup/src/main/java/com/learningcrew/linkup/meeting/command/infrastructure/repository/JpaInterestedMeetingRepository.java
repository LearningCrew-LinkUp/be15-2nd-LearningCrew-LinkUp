package com.learningcrew.linkup.meeting.command.infrastructure.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.InterestedMeeting;
import com.learningcrew.linkup.meeting.command.domain.repository.InterestedMeetingRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaInterestedMeetingRepository extends InterestedMeetingRepository, JpaRepository<InterestedMeeting, Integer> {
    boolean existsByInterestedMeetingId_MeetingIdAndInterestedMeetingId_MemberId(int meetingId, int memberId);
}
