package com.learningcrew.linkup.point.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "회원 포인트 내역 조회 응답")
public class UserPointTransactionResponse {

    @Schema(description = "포인트 거래 내역 id")
    private int pointTransactionId;

    @Schema(description = "거래 유형 (CHARGE, PAYMENT, REFUND, WITHDRAW)")
    private String transactionType;

    @Schema(description = "거래 일시")
    private LocalDateTime createdAt;

    @Schema(description = "거래 금액")
    private int amount;
}
