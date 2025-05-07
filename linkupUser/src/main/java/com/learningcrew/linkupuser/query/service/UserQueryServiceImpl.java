package com.learningcrew.linkupuser.query.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learningcrew.linkupuser.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkupuser.common.domain.Status;
import com.learningcrew.linkupuser.common.dto.PageResponse;
import com.learningcrew.linkupuser.common.dto.query.RoleDTO;
import com.learningcrew.linkupuser.common.query.mapper.RoleMapper;
import com.learningcrew.linkupuser.common.util.SecurityUtils;
import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import com.learningcrew.linkupuser.query.dto.constants.RoleType;
import com.learningcrew.linkupuser.query.dto.query.*;
import com.learningcrew.linkupuser.query.dto.response.*;
import com.learningcrew.linkupuser.query.mapper.MemberMapper;
import com.learningcrew.linkupuser.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final MemberMapper memberMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public UserProfileResponse getUserProfile(int userId) {
        MemberProfileDto memberProfileDTO = Optional.ofNullable(memberMapper.getUserProfileByEmail(userId))
                .orElseThrow(() -> new RuntimeException("유저 정보 찾지 못함"));

        return UserProfileResponse
                .builder()
                .member(memberProfileDTO)
                .build();
    }

    @Override
    public PageResponse<UserListResponse> getUserList(String roleName, String statusName, int page, int size) {
        PageHelper.startPage(page, size);

        List<UserListResponse> list = userMapper.findUserList(roleName, statusName);

        PageInfo<UserListResponse> pageInfo = new PageInfo<>(list);

        return PageResponse.from(pageInfo);
    }

    /* 매너 온도 조회 */
    @Override
    public UserMannerTemperatureDto getMannerTemperature(int userId) {
        return memberMapper.findUserMannerTemperature(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_MANNER_TEMPERATURE)
        );
    }


    @Override
    public MeetingMemberDto getMeetingMember(int memberId) {
        return userMapper.findMeetingMemberById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER)
        );
    }


    @Override
    public Boolean getExistsUser(int userId) {
        return userMapper.existsByUserId(userId);
    }

    @Override
    public String getUserEmail(int userId) {
        return userMapper.findUserEmailByUserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public String getUserNameByUserId(int userId) {
        return userMapper.findUserNameByUserId(userId).orElseThrow(
                ()->new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @Override
    public int getPointBalance(int userId) {
        return userMapper.findPointByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_POINT)
        ).getTotalPoints();
    }

    @Override
    public UserStatusResponse getUserStatus(int userId) {
        return userMapper.findStatusByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_STATUS)
        );
    }

    @Override
    public UserDetailResponse getUser(int userId) {
        return userMapper.findUser(userId);
    }

    @Override
    public UserMypageResponse getUserMypage(int userId) {

        /* 활성화된 회원인지 확인 */
        UserStatusResponse status = userMapper.findStatusByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_STATUS)
        );

        if(!status.getStatus().getStatusType().equals(LinkerStatusType.ACCEPTED.name())){
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return userMapper.findUserMypageById(userId);
    }

    @Override
    public BusinessMypageResponse getBusinessMypage(int userId) {

        /* 활성화된 회원인지 확인(pending 포함) */
        UserStatusResponse status = userMapper.findStatusByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_STATUS)
        );

        List<String> allowedStatuses = List.of(
                LinkerStatusType.ACCEPTED.name(),
                LinkerStatusType.PENDING.name()
        );

        String statusType = status.getStatus().getStatusType();
        if (!allowedStatuses.contains(statusType)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return userMapper.findBusinessMypageById(userId);
    }


}
