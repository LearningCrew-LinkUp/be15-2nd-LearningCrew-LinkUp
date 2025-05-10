package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.common.infrastructure.MemberQueryClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingStatus;
import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.*;
import com.learningcrew.linkup.meeting.query.mapper.MeetingMapper;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingMapper meetingMapper;
    private final MemberQueryClient memberQueryClient;
    private final PlaceQueryService placeQueryService;

    public MeetingListResponse getMeetings(MeetingSearchRequest request) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectMeetings(request);
        long total = meetingMapper.countMeetings(request);

        // 상태 enum 매핑
        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
            Integer placeId = m.getPlaceId();
            if (placeId != null) {
                m.setPlace(placeQueryService.getPlaceDetails(placeId));
                m.setParticipationFee((double) m.getPlace().getRentalCost() / m.getMinUser());
            }
            if (placeId == null) {
                m.setParticipationFee(0);
            }

            m.setParticipantCount(getParticipantCount(m.getMeetingId()));
            m.setInterestedCount(getInterestedCount(m.getMeetingId()));
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalItems(total)
                        .totalPage((int) Math.ceil((double) total / request.getSize()))
                        .build())
                .build();
    }

    public MeetingDetailResponse getMeetingDetail(int meetingId) {
        // 모임 정보 조회
        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);
        meeting.setLeader(memberQueryClient.getMemberById(meeting.getLeaderId()).getData());
        meeting.setParticipantCount(getParticipantCount(meetingId));
        meeting.setInterestedCount(getInterestedCount(meetingId));

        Integer placeId = meeting.getPlaceId();
        if (placeId != null) {
            meeting.setPlace(placeQueryService.getPlaceDetails(placeId));
            meeting.setParticipationFee((double) meeting.getPlace().getRentalCost() / meeting.getMinUser());
        }
        if (placeId == null) {
            meeting.setParticipationFee(0);
        }

        if (meeting == null) {
            throw new BusinessException(ErrorCode.MEETING_NOT_FOUND);
        }

        // 상태 한글 이름 설정
        MeetingStatus status = MeetingStatus.fromId(meeting.getStatusId());
        meeting.setStatusName(status.getLabel());

        // 상태가 ACCEPTED일 때만 참가자 목록 조회 -> 아래로 이관

        return MeetingDetailResponse.builder()
                .meeting(meeting)
                .build();
    }

    public ParticipantsResponse getParticipants(int meetingId, int requesterId) {
        List<MemberDTO> participants = meetingMapper.selectParticipantsByMeetingId(meetingId);

        // 유효한 요청인지 확인
        boolean isValidRequest = participants.stream()
                .map(MemberDTO::getMemberId)
                .anyMatch(x -> x == requesterId);

        if (!isValidRequest) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참가자 조회 권한이 없습니다.");
        }

        return ParticipantsResponse.builder()
                .participants(participants)
                .build();
    }

    public MeetingListResponse getAcceptedMeetingsByUser(int userId) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectAcceptedMeetingsByUserId(userId);

        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(null) // 단건/전체 반환이므로 페이징 불필요
                .build();
    }

    public MeetingListResponse getPastMeetingsByUser(int userId) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectPastMeetingsByUserId(userId, LocalDate.now());

        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(null)
                .build();
    }

    public MeetingListResponse getInterestedMeetings(int userId) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectInterestedMeetings(userId);

        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(null)
                .build();
    }

    @Transactional(readOnly = true)
    public List<MemberDTO> getPendingParticipantsByMeetingId(int meetingId, int requesterId) {
        // 1. 모임이 존재하는지, 그리고 요청자가 리더인지 확인
        MeetingDTO meeting = meetingMapper.selectMeetingById(meetingId);
        if (meeting == null) {
            throw new BusinessException(ErrorCode.MEETING_NOT_FOUND);
        }

        if (meeting.getLeaderId() != requesterId) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "해당 모임에 대한 권한이 없습니다.");
        }

        // 2. 참가 요청 목록 조회
        return meetingMapper.getPendingParticipantsByMeetingId(meetingId);
    }


    public MeetingListResponse getPendingMeetingsByUser(int userId) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectPendingMeetingsByUserId(userId);

        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(null)
                .build();
    }

    public MeetingListResponse getCreatedMeetingsByUser(int userId) {
        List<MeetingSummaryDTO> meetings = meetingMapper.selectCreatedMeetingsByUserId(userId);

        meetings.forEach(m -> {
            MeetingStatus status = MeetingStatus.fromId(m.getStatusId());
            m.setStatusName(status.getLabel());
        });

        return MeetingListResponse.builder()
                .meetings(meetings)
                .pagination(null)
                .build();
    }

    public int getParticipantCount(int meetingId) {
        return meetingMapper.countParticipantsByMeetingId(meetingId);
    }

    public int getInterestedCount(int meetingId) {
        return meetingMapper.countInterestedMembersByMeetingId(meetingId);
    }
}
