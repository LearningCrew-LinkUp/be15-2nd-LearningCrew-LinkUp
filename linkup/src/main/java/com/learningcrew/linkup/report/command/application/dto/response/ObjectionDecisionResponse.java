package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "이의 제기 처리 응답 DTO")
public class ObjectionDecisionResponse {

    @Schema(description = "이의 제기 ID", example = "3001")
    private Long objectionId;

    @Schema(description = "처리 상태 ID (2: 승인, 3: 거절)", example = "2")
    private Integer statusId;

    @Schema(description = "처리 상태 이름", example = "승인")
    private String statusName;

    @Schema(description = "처리 일시", example = "2025-05-05T14:30:00")
    private LocalDateTime processedAt;

    @Schema(description = "처리 결과 메시지", example = "이의 제기가 승인되었습니다.")
    private String message;
}
