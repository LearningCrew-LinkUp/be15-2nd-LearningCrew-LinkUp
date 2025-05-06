package com.learningcrew.linkup.point.command.application.controller;

import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.request.WithdrawRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.application.service.PointCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
@Tag(name="포인트 충전" , description = "포인트 충전 API")
public class PointCommandController {
    private final PointCommandService pointCommandService;

    /* 포인트 충전 */
    @Operation(
            summary = "포인트 충전",
            description = "회원은 포인트를 충전한다."
    )
    @PostMapping("/charge")
    public ResponseEntity<PointTransactionResponse> createPointTransaction(
            @RequestBody PointTransactionRequest request
    ){
        PointTransactionResponse response = pointCommandService.createPointTransaction(request);
        return ResponseEntity.ok(response);
    }

    /* 포인트 환급 */
    @PostMapping("/refund")
    public ResponseEntity<PointTransactionResponse> refundPointTransaction(
            @RequestBody WithdrawRequest request
    )
    {
        PointTransactionResponse response = pointCommandService.withdrawPoint(request);
        return ResponseEntity.ok(response);
    }

    /* 정산 출금 */
}
