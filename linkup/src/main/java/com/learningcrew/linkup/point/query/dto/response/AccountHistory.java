package com.learningcrew.linkup.point.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "계좌 목록 조회 응답 DTO")
public class AccountHistory {
    @Schema(description = "계좌 id")
    private String accountId;

    @Schema(description = "사용자 id")
    private String userId;

    @Schema(description = "권한명")
    private String roleName;

    @Schema(description = "상태타입")
    private String statusType;

    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @Schema(description = "계좌번호", example = "12345678901234")
    private String accountNumber;

    @Schema(description = "예금주", example = "홍길동")
    private String holderName;

    @Schema(description = "등록일", example = "2024-05-30")
    private String createdAt;
}