package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;

import java.util.List;
import java.util.Optional;

public interface MeetingParticipationHistoryRepository {
    MeetingParticipationHistory save(MeetingParticipationHistory history);

    void delete(MeetingParticipationHistory history);

    List<MeetingParticipationHistory> findByMeetingIdAndStatusId(int meetingId, int statusId);

    void flush();
    Optional<MeetingParticipationHistory> findByMeetingIdAndMemberId(int meetingId, int memberId);
    List<MeetingParticipationHistory> findAllByMeetingIdAndStatusId(int meetingId, int accepted);

    boolean existsByMeetingIdAndMemberId(int meetingId, int memberId);

    Optional<MeetingParticipationHistory> findByMeetingIdAndMemberIdAndStatusId(int meetingId, int memberId, int acceptedStatusId);

    boolean existsByMeetingIdAndMemberIdAndStatusId(int meetingId, int memberId, int statusAccepted);

    List<MeetingParticipationHistory> findAllByMeetingId(int meetingId);

    List<MeetingParticipationHistory> findByMemberIdAndMeetingIdAndStatusId(Integer memberId, Integer meetingId, int statusReviewWritten);
}
