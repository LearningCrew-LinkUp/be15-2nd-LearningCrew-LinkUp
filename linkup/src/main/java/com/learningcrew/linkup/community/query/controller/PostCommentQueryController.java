package com.learningcrew.linkup.community.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.community.query.dto.request.CommunitySearchRequest;
import com.learningcrew.linkup.community.query.dto.response.PostCommentListResponse;
import com.learningcrew.linkup.community.query.service.PostCommentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 조회 ", description = "관리자 및 사용자의 댓글 조회 API")
public class PostCommentQueryController {

    private final PostCommentQueryService postCommentQueryService;

    @GetMapping
    @Operation(summary = "전체 댓글 조회 (관리자용)", description = "관리자가 서비스 내에 작성된 모든 댓글을 조회한다.")
    public ResponseEntity<ApiResponse<PostCommentListResponse>> getAllComments(CommunitySearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(postCommentQueryService.getAllComments(request)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "특정 회원의 댓글 목록 조회", description = "관리자 혹은 특정 사용자가 해당 사용자의 작성 댓글 목록을 조회한다.")
    public ResponseEntity<ApiResponse<PostCommentListResponse>> getCommentsByUser(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(postCommentQueryService.getCommentsByUser(userId, page, size)));
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "게시글의 댓글 목록 조회", description = "해당 게시글에 달린 댓글들을 조회하고 좋아요 여부도 포함한다.")
    public ResponseEntity<ApiResponse<PostCommentListResponse>> getCommentsForPost(
            @PathVariable int postId,
            @RequestParam int userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                postCommentQueryService.getCommentsWithLikes(postId, userId)
        ));
    }
}