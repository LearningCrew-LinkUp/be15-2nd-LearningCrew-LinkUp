package com.learningcrew.linkupuser.query.controller;

import com.learningcrew.linkupuser.client.CommunityClient;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.query.dto.query.UserCommentDto;
import com.learningcrew.linkupuser.query.dto.query.UserPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feign-test/community")
@RequiredArgsConstructor
public class CommunityTestController {

    private final CommunityClient communityFeignClient;

    /**
     * 작성한 게시글 테스트 조회
     */
    @GetMapping("/user/{userId}/posts")
    public ApiResponse<List<UserPostDto>> getUserPosts(@PathVariable int userId) {
        return communityFeignClient.getPostsByUser(userId);
    }

    /**
     * 작성한 댓글 테스트 조회ㅌ
     */
    @GetMapping("/user/{userId}/comments")
    public ApiResponse<List<UserCommentDto>> getUserComments(@PathVariable int userId) {
        return communityFeignClient.getCommentsByUser(userId);
    }
}
