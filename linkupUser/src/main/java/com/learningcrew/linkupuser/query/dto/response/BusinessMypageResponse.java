package com.learningcrew.linkupuser.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessMypageResponse {
    private String userName;
    private String businessStatus; // 승인/대기/거절 등
}
