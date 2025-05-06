package com.learningcrew.linkup.place.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learningcrew.linkup.common.dto.PageResponse;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.place.query.dto.response.OwnerResponse;
import com.learningcrew.linkup.place.query.mapper.OwnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerQueryServiceImpl implements OwnerQueryService {

    private final OwnerMapper ownerMapper;

    @Override
    @Transactional(readOnly = true)
    public OwnerResponse getOwnerInfo(int ownerId) {
        //단일 사업자 조회
        return ownerMapper.findByOwnerId(ownerId).orElseThrow(
                () -> new BusinessException(ErrorCode.OWNER_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OwnerResponse> findAllOwners(String statusName, int page, int size) {
        PageHelper.startPage(page, size);

        List<OwnerResponse> list = ownerMapper.findAllOwners(statusName);

        PageInfo<OwnerResponse> pageInfo = new PageInfo<>(list);

        return PageResponse.from(pageInfo);
    }

}