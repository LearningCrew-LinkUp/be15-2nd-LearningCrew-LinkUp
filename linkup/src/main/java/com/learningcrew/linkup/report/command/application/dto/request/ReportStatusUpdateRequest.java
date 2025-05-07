package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "신고 상태 업데이트 요청 DTO")
public class ReportStatusUpdateRequest {

    @Schema(description = "신고 처리 상태 ID (2: 처리 완료, 3: 거절 등)", example = "2")
    private Integer statusId;

    @Schema(description = "관리자의 처리 사유 (선택)", example = "신고 내용이 확인되어 처리 완료 상태로 변경합니다.")
    private String reason;
}
