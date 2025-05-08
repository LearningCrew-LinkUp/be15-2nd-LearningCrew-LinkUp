package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.meeting.command.domain.repository.MeetingQueryRepository;
import com.learningcrew.linkup.meeting.query.dto.response.ReservedTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingQueryService {

    private final MeetingQueryRepository meetingQueryRepository;

    public List<ReservedTimeResponse> getReservedTimes(int placeId, LocalDate date) {
        return meetingQueryRepository.findReservedTimes(placeId, date);
    }
}