package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingSummaryDTO;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMeetingMapper {
    List<MeetingSummaryDTO> selectAllMeetings(MeetingSearchRequest request);
    long countAllMeetings(MeetingSearchRequest request);

}
