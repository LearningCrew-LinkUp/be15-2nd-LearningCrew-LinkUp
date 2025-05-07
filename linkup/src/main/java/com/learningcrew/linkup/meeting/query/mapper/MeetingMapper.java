package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingSummaryDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MeetingMapper {

    List<MeetingSummaryDTO> selectMeetings(MeetingSearchRequest request);
    long countMeetings(MeetingSearchRequest request);

    MeetingDTO selectMeetingById(int meetingId);
    List<MemberDTO> selectParticipantsByMeetingId(int meetingId);

    List<MeetingSummaryDTO> selectAcceptedMeetingsByUserId(int userId);
    List<MeetingSummaryDTO> selectPastMeetingsByUserId(@Param("userId") int userId, @Param("now") LocalDate now);

    List<MeetingSummaryDTO> selectInterestedMeetings(int userId);

    /* 모임 ID로 참가 내역 목록 조회 */
//    List<MeetingParticipationDTO> selectHistoriesByMeetingId(int meetingId);

    List<MemberDTO> getPendingParticipantsByMeetingId(int meetingId);

    List<MeetingSummaryDTO> selectPendingMeetingsByUserId(int userId);

    List<MeetingSummaryDTO> selectCreatedMeetingsByUserId(int userId);

    int countParticipantsByMeetingId(int meetingId);

    int countInterestedMembersByMeetingId(int meetingId);
}
