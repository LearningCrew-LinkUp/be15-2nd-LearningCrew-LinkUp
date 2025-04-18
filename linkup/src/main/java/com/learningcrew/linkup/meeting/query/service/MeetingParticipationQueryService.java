package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.meeting.query.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingParticipationQueryService {
    private final MeetingMapper meetingMapper;



}
