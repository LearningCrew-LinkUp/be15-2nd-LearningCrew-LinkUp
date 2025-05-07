package com.learningcrew.linkup.community.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.community.command.application.dto.PostImageResponse;
import com.learningcrew.linkup.community.command.application.service.PostImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class PostImageCommandController {

    private final PostImageCommandService postImageCommandService;

    @PutMapping("/posts/{postId}/images")
    public ResponseEntity<ApiResponse<PostImageResponse>> updatePostImages(
            @PathVariable int postId,
            @RequestParam(name = "postImgs", required = false) List<MultipartFile> postImgs) {

        PostImageResponse response = postImageCommandService.updatePostImages(postId, postImgs);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}