package com.learningcrew.linkup.point.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "포인트 트랜잭션 응답 데이터")
public class PointTransactionResponse {

    @Schema(description = "포인트 트랜잭션 ID")
    private Long pointTransactionId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "권한 이름")
    private String roleName;

    @Schema(description = "포인트 금액")
    private Integer amount;

    @Schema(description = "거래 유형")
    private String transactionType;

    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
}
