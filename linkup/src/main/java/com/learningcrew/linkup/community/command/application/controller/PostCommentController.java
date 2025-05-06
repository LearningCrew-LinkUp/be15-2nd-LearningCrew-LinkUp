package com.learningcrew.linkup.community.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.community.command.application.dto.PostCommentCreateRequest;
import com.learningcrew.linkup.community.command.application.dto.PostCommentResponse;
import com.learningcrew.linkup.community.command.application.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<PostCommentResponse>> createComment
            (@PathVariable int postId,
            @RequestBody PostCommentCreateRequest commentRequest) {
        PostCommentResponse response = postCommentService.createComment(postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/{commentId}/delete")
        public ResponseEntity<ApiResponse<PostCommentResponse>> deleteComment(
        @PathVariable("postId") int postId,
        @PathVariable("commentId") BigInteger commentId,
        @RequestParam  int userId) {
        postCommentService.softDeleteComment(postId, commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null));
        }
    }
