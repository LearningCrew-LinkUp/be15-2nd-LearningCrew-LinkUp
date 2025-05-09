package com.learningcrew.linkup.place.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.service.FileStorage;
import com.learningcrew.linkup.place.command.application.dto.request.PlaceReviewCreateRequest;
import com.learningcrew.linkup.place.command.application.dto.response.PlaceCommandResponse;
import com.learningcrew.linkup.place.command.application.dto.response.PlaceImageResponse;
import com.learningcrew.linkup.place.command.application.service.PlaceReviewCommandService;
import com.learningcrew.linkup.place.query.dto.response.PlaceReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name="장소 리뷰 관리", description="장소 리뷰 등록 API")
public class PlaceReviewCommandController {

    private final PlaceReviewCommandService placeReviewCommandService;
    private final FileStorage fileStorage;

    @Operation(
            summary = "장소리뷰 등록",
            description = "회원은 자신이 참여한 모임의 장소에 대한 리뷰를 수정한다."
    )
    @PostMapping("/place/{placeId}/review")
    public ResponseEntity<ApiResponse<PlaceReviewResponse>> createPlaceReview(
            @PathVariable int placeId,
            @RequestPart("data") @Valid PlaceReviewCreateRequest request,
            @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage) {

        if (reviewImage != null && !reviewImage.isEmpty()) {
            String storedFileName = fileStorage.storeFile(reviewImage);
            String imageUrl = "/images/" + storedFileName;
            request.setReviewImageUrl(imageUrl);
        }

        PlaceReviewResponse response = placeReviewCommandService.createReview(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}