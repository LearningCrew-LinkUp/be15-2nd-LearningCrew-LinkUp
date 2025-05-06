package com.learningcrew.linkup.place.query.mapper;

import com.learningcrew.linkup.place.query.dto.response.OwnerResponse;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OwnerMapper {
    Optional<OwnerResponse> findByOwnerId(@Param("ownerId") int ownerId);
    List<OwnerResponse> findAllOwners(String statusName);
}
