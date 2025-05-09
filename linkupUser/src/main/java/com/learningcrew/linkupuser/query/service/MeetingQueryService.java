package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.common.dto.PageResponse;
import com.learningcrew.linkupuser.query.dto.query.MeetingHistorySearchCondition;
import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;
import com.learningcrew.linkupuser.query.dto.response.MeetingHistoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingQueryService {

    List<UserMeetingDto> findMeetingsByUser(int userId);

    PageResponse<MeetingHistoryResponse> getMeetingHistory(int userId, MeetingHistorySearchCondition condition, Pageable pageable);
}

