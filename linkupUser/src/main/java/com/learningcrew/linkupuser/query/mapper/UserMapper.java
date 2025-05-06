package com.learningcrew.linkupuser.query.mapper;


import com.learningcrew.linkupuser.command.domain.aggregate.User;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.common.dto.PageResponse;
import com.learningcrew.linkupuser.query.dto.query.*;
import com.learningcrew.linkupuser.query.dto.response.UserDetailResponse;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.dto.response.UserStatusResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    PageResponse<UserListResponse> findAllUsers(int roleId);

    List<UserMeetingDto> findUserMeetings(int userId);

    Optional<UserPointDto> findPointByUserId(int userId);

    Optional<MeetingMemberDto> findMeetingMemberById(int memberId);

    Boolean existsByUserId(int userId);

    Optional<String> findUserEmailByUserId(int userId);

    Optional<String> findUserNameByUserId(int userId);

    Optional<UserStatusResponse> findStatusByUserId(int userId);

    List<UserListResponse> findUserList(String roleName, String statusName);

    UserDetailResponse findUser(int userId);
}
