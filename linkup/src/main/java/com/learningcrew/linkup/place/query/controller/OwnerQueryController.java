package com.learningcrew.linkup.place.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.PageResponse;
import com.learningcrew.linkup.place.query.dto.response.OwnerResponse;
import com.learningcrew.linkup.place.query.service.OwnerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "사업자 권한 조회", description = "Owner Query API")
public class OwnerQueryController {

    private final OwnerQueryService ownerQueryService;

    // 단건 조회
    @Operation(summary = "단일 사업자 신청 조회")
    @GetMapping("/businesses/{targetId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> getOwner(
            @PathVariable int targetId
    ) {
        OwnerResponse response = ownerQueryService.getOwnerInfo(targetId);
        return ResponseEntity.ok(ApiResponse.success(response, "사업자 정보 조회 성공"));
    }

    // 사업자 전체 조회
    @Operation(summary = "모든 사업자 조회")
    @GetMapping("/businesses")
    public ResponseEntity<ApiResponse<PageResponse<OwnerResponse> >> getAllOwners(
            @RequestParam(value = "statusName", required = false) String statusName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageResponse<OwnerResponse> responseList = ownerQueryService.findAllOwners(statusName, page, size);
        return ResponseEntity.ok(ApiResponse.success(responseList, "사업자 전체 조회 성공"));
    }
}