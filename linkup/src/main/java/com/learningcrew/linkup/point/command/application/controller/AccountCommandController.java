package com.learningcrew.linkup.point.command.application.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.point.command.application.dto.request.AccountRequest;
import com.learningcrew.linkup.point.command.application.service.PointAccountCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "계좌", description = "계좌 Command API")
public class AccountCommandController {

    private final PointAccountCommandService accountService;

    @PostMapping
    @Operation(summary = "계좌 등록")
    public ResponseEntity<ApiResponse<Void>> registerAccount(
            @RequestBody @Valid AccountRequest request,
            @AuthenticationPrincipal String userId
    ) {
        accountService.register(Integer.parseInt(userId), request);
        return ResponseEntity.ok(ApiResponse.success(null, "계좌 등록 성공"));
    }

    @PutMapping
    @Operation(summary = "계좌 수정")
    public ResponseEntity<ApiResponse<Void>> updateAccount(
            @RequestBody @Valid AccountRequest request,
            @AuthenticationPrincipal String userId
    ) {
        accountService.update(Integer.parseInt(userId), request);
        return ResponseEntity.ok(ApiResponse.success(null,"계좌 수정 성공"));
    }
}

