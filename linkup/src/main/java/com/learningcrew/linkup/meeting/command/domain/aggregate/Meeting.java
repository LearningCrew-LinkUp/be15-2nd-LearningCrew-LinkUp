package com.learningcrew.linkup.meeting.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meetingId;
    @Setter
    private int leaderId;
    private Integer placeId;
    @Setter
    private int sportId;
    @Setter
    private int statusId;
    private String meetingTitle;
    private String meetingContent;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minUser;
    private int maxUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private MeetingGender gender;
    private String ageGroup;
    private String level;
    private String customPlaceAddress;
    private Double latitude;
    private Double longitude;

}