package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingStatus;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingListResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingSummaryDTO;
import com.learningcrew.linkup.meeting.query.dto.response.ParticipantReviewDTO;
import com.learningcrew.linkup.meeting.query.mapper.AdminMeetingMapper;
import com.learningcrew.linkup.meeting.query.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMeetingService {

    private final AdminMeetingMapper adminMeetingMapper;
    private final ReviewMapper reviewMapper;

    public MeetingListResponse getAllMeetings(MeetingSearchRequest request) {
        List<MeetingSummaryDTO> meetings = adminMeetingMapper.selectAllMeetings(request);
        long totalItems = adminMeetingMapper.countAllMeetings(request);

        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalItems(totalItems)
                        .totalPage((int) Math.ceil((double) totalItems / request.getSize()))
                        .build())
                .build();
    }

    public List<ParticipantReviewDTO> getAllParticipantReviews() {
        return reviewMapper.selectAllParticipantReviews();
    }

    public List<ParticipantReviewDTO> getReviewsByReviewer(int memberId) {
        return reviewMapper.selectReviewsByReviewer(memberId);
    }

    public List<ParticipantReviewDTO> getReviewsByReviewee(int memberId) {
        return reviewMapper.selectReviewsByReviewee(memberId);
    }


}
