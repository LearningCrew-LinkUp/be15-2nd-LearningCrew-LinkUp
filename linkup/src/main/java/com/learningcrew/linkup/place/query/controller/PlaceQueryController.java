package com.learningcrew.linkup.place.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.place.query.dto.request.PlaceListRequest;
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
@RequestMapping("")
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
    @GetMapping("/admin/places")
    @Operation(summary = "관리자 장소 목록 조회", description = "관리자가 서비스에 등록된 모든 장소의 목록을 조회한다.")
    public ResponseEntity<ApiResponse<PlaceListResponse>> getPlacesByAdmin(PlaceListRequest placeListRequest) {
        PlaceListResponse response = placeQueryService.getPlacesByAdmin(placeListRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/owner/{ownerId}/places")
    @Operation(summary = "사업자 장소 목록 조회", description = "사업자가 자신이 등록한 장소의 목록을 조회한다.")
    public ResponseEntity<ApiResponse<PlaceListResponse>> getPlacesByOwner(
            @PathVariable("ownerId") Integer ownerId,
            PlaceListRequest request) {

        request.setOwnerId(ownerId); // ⬅️ 필수 세팅
        PlaceListResponse response = placeQueryService.getPlacesByOwner(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
