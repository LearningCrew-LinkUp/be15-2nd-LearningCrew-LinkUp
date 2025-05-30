package com.learningcrew.linkup.community.query.mapper;

import com.learningcrew.linkup.common.dto.query.UserPostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {
    List<UserPostDto> findPostsByUserId(int userId);
}
