package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "신고 처리 결과 응답 DTO")
public class ReportHandleResponse {

    @Schema(description = "처리된 신고 ID", example = "4001")
    private Long reportId;

    @Schema(description = "처리 상태 ID (2: 처리 완료, 3: 거절 등)", example = "2")
    private Integer statusId;

    @Schema(description = "처리 결과 메시지", example = "신고가 정상적으로 처리되었습니다.")
    private String message;
}
