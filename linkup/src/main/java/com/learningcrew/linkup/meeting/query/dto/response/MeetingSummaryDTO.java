package com.learningcrew.linkup.meeting.query.dto.response;

import com.learningcrew.linkup.place.query.dto.response.PlaceDetailResponse;
import com.learningcrew.linkup.place.query.dto.response.PlaceDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MeetingSummaryDTO {
    private int meetingId;
    private String leaderNickname;
    private Integer placeId;
    private PlaceDetailResponse place;
    private String placeName;
    private String placeAddress;
    private String sportName;
    private int statusId;
    private String statusName;
    private String meetingTitle;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minUser;
    private int maxUser;
    private String gender;
    private String ageGroup;
    private String level;
    private String customPlaceAddress;
    private int participantCount;
    private int interestedCount;
    private double participationFee;
}
