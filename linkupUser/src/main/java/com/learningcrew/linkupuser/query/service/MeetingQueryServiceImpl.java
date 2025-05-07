package com.learningcrew.linkupuser.query.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learningcrew.linkupuser.common.dto.PageResponse;
import com.learningcrew.linkupuser.query.dto.query.MeetingHistorySearchCondition;
import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;
import com.learningcrew.linkupuser.query.dto.response.MeetingHistoryResponse;
import com.learningcrew.linkupuser.query.mapper.UserMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Schema
@Service
@RequiredArgsConstructor
public class MeetingQueryServiceImpl implements MeetingQueryService {
    private final UserMapper userMapper;

    /* 참가 모임 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<UserMeetingDto> findMeetingsByUser(int userId) {
        return userMapper.findUserMeetings(userId);
    }

    public PageResponse<MeetingHistoryResponse> getMeetingHistory(int userId, MeetingHistorySearchCondition condition, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<MeetingHistoryResponse> meetings = userMapper.findMeetingHistory(condition);
        PageInfo<MeetingHistoryResponse> pageInfo = new PageInfo<>(meetings);
        return PageResponse.from(pageInfo);
    }
}
