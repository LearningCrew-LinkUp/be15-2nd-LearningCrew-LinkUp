package com.learningcrew.linkup.place.query.service;

import com.learningcrew.linkup.common.dto.PageResponse;
import com.learningcrew.linkup.place.query.dto.response.OwnerResponse;

import java.util.List;

public interface OwnerQueryService {
    OwnerResponse getOwnerInfo(int ownerId);
    PageResponse<OwnerResponse> findAllOwners(String statusName, int page, int size);

}

