package com.learningcrew.linkup.place.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.query.dto.request.AdminPlaceListRequest;
import com.learningcrew.linkup.place.query.dto.request.AdminPlaceReviewListRequest;
import com.learningcrew.linkup.place.query.dto.request.PlaceListRequest;
import com.learningcrew.linkup.place.query.dto.response.AdminPlaceListResponse;
import com.learningcrew.linkup.place.query.dto.response.AdminPlaceReviewListResponse;
import com.learningcrew.linkup.place.query.dto.response.PlaceDetailResponse;
import com.learningcrew.linkup.place.query.dto.response.PlaceListResponse;
import com.learningcrew.linkup.place.query.service.PlaceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "장소 조회", description = "등록된 장소 조회 API")
public class PlaceQueryController {
    private final PlaceQueryService placeQueryService;

    @GetMapping("/places")
    @Operation(summary = "예약 가능 장소 목록 조회", description = "회원이 예약가능한 장소의 목록을 조회합니다.(검색 포함)")
    public ResponseEntity<ApiResponse<PlaceListResponse>> getPlaces(PlaceListRequest placeListRequest) {
        PlaceListResponse response = placeQueryService.getPlaces(placeListRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/place/{placeId}")
    @Operation(summary = "장소 상세 조회", description = "회원이 서비스에 등록된 장소의 세부 설명과 주소 그리고 해당 장소에 작성된 장소후기를 상세 페이지를 통해 조회합니다.")
    public ResponseEntity<ApiResponse<PlaceDetailResponse>> getPlaceDetails(@PathVariable("placeId") int placeId) {
        PlaceDetailResponse detailResponse = placeQueryService.getPlaceDetails(placeId);
        return ResponseEntity.ok(ApiResponse.success(detailResponse));
    }

    @GetMapping("/api/v1/common-service/places/{placeId}")
    @Operation(summary = "장소 상세 (단순 정보) 조회", description = "프론트 예약용 모달 등에서 사용하는 장소 기본 정보 및 운영 시간만 조회합니다.")
    public ResponseEntity<ApiResponse<PlaceDetailResponse>> getPlaceDetailById(@PathVariable("placeId") int placeId) {
        PlaceDetailResponse detail = placeQueryService.getPlaceDetailById(placeId);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @GetMapping("/admin/places")
    @Operation(summary = "관리자 장소 목록 조회", description = "관리자가 서비스에 등록된 모든 장소의 목록을 조회한다.")
    public ResponseEntity<ApiResponse<AdminPlaceListResponse>> getPlacesByAdmin(AdminPlaceListRequest placeListRequest) {
        AdminPlaceListResponse response = placeQueryService.getPlacesByAdmin(placeListRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/owner/{ownerId}/places")
    @Operation(summary = "사업자 장소 목록 조회", description = "사업자가 자신이 등록한 장소의 목록을 조회한다.")
    public ResponseEntity<ApiResponse<PlaceListResponse>> getPlacesByOwner(
            @PathVariable("ownerId") Integer ownerId,
            PlaceListRequest request) {

        request.setOwnerId(ownerId);
        PlaceListResponse response = placeQueryService.getPlacesByOwner(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/admin/place-reviews")
    @Operation(summary = "장소 후기 내역 조회 (관리자)", description = "관리자가 모든 장소의 후기 내역을 조회한다.")
    public ResponseEntity<ApiResponse<AdminPlaceReviewListResponse>> getPlaceReviewsByAdmin(
            AdminPlaceReviewListRequest request) {

        AdminPlaceReviewListResponse response = placeQueryService.getPlaceReviewsByAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }




}
