package com.learningcrew.linkupuser.query.service;

import com.learningcrew.linkupuser.common.dto.PageResponse;
import com.learningcrew.linkupuser.query.dto.query.MeetingMemberDto;
import com.learningcrew.linkupuser.query.dto.query.UserMannerTemperatureDto;
import com.learningcrew.linkupuser.query.dto.response.*;


public interface UserQueryService {
    UserProfileResponse getUserProfile(int userId);
    PageResponse<UserListResponse> getUserList(String roleName, String statusName, int page, int size);
    UserMannerTemperatureDto getMannerTemperature(int userId);


    MeetingMemberDto getMeetingMember(int memberId);

    Boolean getExistsUser(int userId);

    String getUserEmail(int userId);

    String getUserNameByUserId(int userId);

    int getPointBalance(int userId);

    UserStatusResponse getUserStatus(int userId);

    UserDetailResponse getUser(int userId);

    UserMypageResponse getUserMypage(int userId);

    BusinessMypageResponse getBusinessMypage(int userId);
}
