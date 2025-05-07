
package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSimpleDTO {

    @Schema(description = "신고 ID", example = "1001")
    private Long reportId;

    @Schema(description = "신고자 ID", example = "35")
    private Long reporterId;

    @Schema(description = "신고자 이름", example = "사용자")
    private String reporterName;

    @Schema(description = "신고 유형 (예: 스팸, 욕설)", example = "욕설")
    private String reportType;

    @Schema(description = "신고 생성 일시", example = "2025-04-30T10:45:00")
    private LocalDateTime createdAt;

    @Schema(description = "신고 처리 상태 (PENDING, APPROVED, REJECTED)", example = "PENDING")
    private Integer statusId;

    @Schema(description = "신고 활성 상태 (Y: 활성, N: 비활성)", example = "Y")
    private String isActive;
}
