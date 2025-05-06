package com.learningcrew.linkupuser.query.dto.response;

import com.learningcrew.linkupuser.query.dto.query.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    private Long userId;
    private String userName;
    private String email;
    private String nickname;
    private String roleName;
    private String statusName;
    private Integer pointBalance;
    private String contactNumber;
}
