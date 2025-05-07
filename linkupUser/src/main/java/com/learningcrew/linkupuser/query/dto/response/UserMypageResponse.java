package com.learningcrew.linkupuser.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMypageResponse {
    private String profileImageUrl;
    private String userName;
    private double mannerTemperature;
    private int point;
}

