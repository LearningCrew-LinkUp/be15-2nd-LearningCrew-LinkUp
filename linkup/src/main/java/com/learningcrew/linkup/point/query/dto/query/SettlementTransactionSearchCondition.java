package com.learningcrew.linkup.point.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "사업자 정산 내역 목록 조회 조건")
public class SettlementTransactionSearchCondition {

    @Schema(description = "사업자 ID")
    private Integer userId;

    @Schema(description = "조회 시작일 (YYYY-MM-DD)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "조회 종료일 (YYYY-MM-DD)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}