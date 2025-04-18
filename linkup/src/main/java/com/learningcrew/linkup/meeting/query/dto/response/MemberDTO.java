package com.learningcrew.linkup.meeting.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberDTO {
    private int memberId;
    private String nickname;
    private String gender;
    private LocalDate birthDate;
    private String introduction;
    private double mannerTemperature;
    private String profileImageUrl;
}
