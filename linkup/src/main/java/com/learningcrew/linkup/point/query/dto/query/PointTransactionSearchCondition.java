package com.learningcrew.linkup.point.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "포인트 트랜잭션 검색 조건")
public class PointTransactionSearchCondition {

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "권한 이름 (ex: USER, ADMIN, BUSINESS)")
    private String roleName;

    @Schema(description = "거래 유형 (ex: CHARGE, PAYMENT, REFUND, WITHDRAW)")
    private String transactionType;

    @Schema(description = "조회 시작일 (YYYY-MM-DD)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "조회 종료일 (YYYY-MM-DD)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
