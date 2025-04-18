package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MeetingDetailResponse {
    private MeetingDTO meeting;
    private List<MemberDTO> participants;
}
