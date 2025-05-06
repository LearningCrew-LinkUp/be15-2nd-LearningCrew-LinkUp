package com.learningcrew.linkupuser.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private Long userId;
    private String userName;
    private String email;
    private String nickname;
    private String roleName;
    private String statusName;
    private Integer pointBalance;
    private String contactNumber;
}
