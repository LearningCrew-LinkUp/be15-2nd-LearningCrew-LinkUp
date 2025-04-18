package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class MeetingDTO {
    private int meetingId;
    private int leaderId;
    private String leaderNickname;
    private Integer placeId;
    private String placeName;
    private String placeAddress;
    private int sportId;
    private String sportName;
    private int statusId;
    private String statusName;
    private String meetingTitle;
    private String meetingContent;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minUser;
    private int maxUser;
    private LocalDateTime createdAt;
    private String gender;
    private String ageGroup;
    private String level;
    private String customPlaceAddress;
}
