package com.learningcrew.linkupuser.query.controller;


import com.learningcrew.linkupuser.common.dto.ApiResponse;
import com.learningcrew.linkupuser.common.dto.PageResponse;
import com.learningcrew.linkupuser.query.dto.query.UserProfileDto;
import com.learningcrew.linkupuser.query.dto.response.UserDetailResponse;
import com.learningcrew.linkupuser.query.dto.response.UserListResponse;
import com.learningcrew.linkupuser.query.service.UserQueryService;
import com.learningcrew.linkupuser.query.service.UserQueryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name="관리자 회원 조회")
public class AdminAuthQueryController {
    private final UserQueryService userQueryService;

    /* 전체 회원 조회 */
    @GetMapping("/users")
    @Operation(description = "전체 회원 조회 (userId 검색, role, status 필터링 가능)")
    public ResponseEntity<ApiResponse<PageResponse<UserListResponse>>> getUserList(
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "statusName", required = false) String statusName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageResponse<UserListResponse> response = userQueryService.getUserList(roleName, statusName, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 단일 회원 조회 */
    @GetMapping("/users/{userId}")
    @Operation(description = "단일 회원 상세 조회")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUser(@PathVariable int userId) {
        UserDetailResponse response = userQueryService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
