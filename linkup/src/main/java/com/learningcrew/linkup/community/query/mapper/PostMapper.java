package com.learningcrew.linkup.community.query.mapper;

import com.learningcrew.linkup.community.command.application.dto.PostCreateRequest;
import com.learningcrew.linkup.community.query.dto.request.CommunitySearchRequest;
import com.learningcrew.linkup.community.query.dto.response.PostDTO;
import com.learningcrew.linkup.community.query.dto.response.PostDetailResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    // 게시글 전체 조회(관리자용)
    List<PostDTO> selectAllPosts(CommunitySearchRequest request);

    // 공지사항 목록 조회 (회원용)
    List<PostDTO> selectNoticePostsForUser(CommunitySearchRequest request);


    // 일반 게시글 목록 조회 (회원용, 페이징)
    List<PostDTO> selectGeneralPostsForUser(CommunitySearchRequest request);

    // 일반 게시글 수 조회
    long countGeneralPostsForUser(CommunitySearchRequest request);


    // 게시글 전체 조회(회원용)
    List<PostDTO> selectAllPostsForUser(CommunitySearchRequest request);

    long countAllPostsForUser(CommunitySearchRequest request);

    // 게시글 수 조회
    long countAllPosts(CommunitySearchRequest request);


    // 게시글 상세 조회
    PostDetailResponse selectPostDetail(int postId);

    // 게시글 이미지 URL 조회
    List<String> selectPostImageUrlsByPostId(int postId);

    // 특정 회원 게시글 조회
    List<PostDTO> selectPostsByUser(@Param("userId") int userId,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    // 특정 회원 게시글 수 조회
    long countPostsByUser(int userId);


}