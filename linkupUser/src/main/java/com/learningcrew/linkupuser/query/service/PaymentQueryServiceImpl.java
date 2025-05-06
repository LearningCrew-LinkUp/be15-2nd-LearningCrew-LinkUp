package com.learningcrew.linkupuser.query.service;

import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import com.learningcrew.linkupuser.query.dto.query.UserPointDto;
import com.learningcrew.linkupuser.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final UserMapper userMapper;

    /* 포인트 조회 */
    @Override
    public UserPointDto findUserPoint(int userId) {
        return userMapper.findPointByUserId(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_POINT)
        );
    }
}
