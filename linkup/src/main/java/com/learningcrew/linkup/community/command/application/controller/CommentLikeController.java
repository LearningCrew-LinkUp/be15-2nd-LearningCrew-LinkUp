package com.learningcrew.linkup.community.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.community.command.application.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> likeComment(@PathVariable int commentId,
                                                         @RequestParam int userId) {
        commentLikeService.likeComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlikeComment(@PathVariable int commentId,
                                                           @RequestParam int userId) {
        commentLikeService.unlikeComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}