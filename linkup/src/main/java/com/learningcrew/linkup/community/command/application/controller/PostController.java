package com.learningcrew.linkup.community.command.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.community.command.application.dto.PostCreateRequest;
import com.learningcrew.linkup.community.command.application.dto.PostResponse;
import com.learningcrew.linkup.community.command.application.dto.PostUpdateRequest;
import com.learningcrew.linkup.community.command.application.service.PostService;
import com.learningcrew.linkup.community.query.dto.response.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestPart("postCreateRequest") PostCreateRequest postCreateRequest,   // @RequestBody -> @RequestPart
            @RequestPart(required = false) List<MultipartFile> postImgs) {  // 파일 업로드를 위한 MultipartFile

        PostResponse response = postService.createPost(postCreateRequest, postImgs);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

//    @PutMapping("/{postId}")
//    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
//            @PathVariable int postId,
//            @RequestPart PostUpdateRequest postUpdateRequest,
//            @RequestPart(required = false) List<MultipartFile> postImgs) {
//
//        int userId = postUpdateRequest.getUserId();  // DTO에서 꺼내기
//        PostResponse response = postService.updatePost(postId, postUpdateRequest, postImgs, userId);
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable int postId,
            @RequestPart("postUpdateRequest") PostUpdateRequest postUpdateRequest, // 수정된 부분
            @RequestPart(value = "postImgs", required = false) List<MultipartFile> postImgs) { // 수정된 부분

        int userId = postUpdateRequest.getUserId();
        PostResponse response = postService.updatePost(postId, postUpdateRequest, postImgs, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }



    @PutMapping("/{postId}/delete")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable int postId,
            @RequestParam int userId) {  // 요청에서 userId를 쿼리 파라미터로 받음
        postService.deletePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}