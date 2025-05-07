package com.learningcrew.linkup.report.command.application.dto.request;

import com.learningcrew.linkup.report.command.domain.aggregate.PenaltyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "신고 처리 및 제재 요청 DTO")
public class HandleReportRequest {

    @Schema(description = "제재 대상 ID (게시글, 댓글, 또는 리뷰 ID)", example = "3456")
    private Long penaltyTargetId;

    @Schema(description = "제재 유형 (POST, COMMENT, REVIEW 중 하나)", example = "COMMENT")
    private PenaltyType penaltyType;

    @Schema(description = "제재 사유", example = "욕설 및 모욕적인 발언으로 인한 제재")
    private String reason;

    @Schema(description = "제재 만료 일시 (null이면 영구 제재)", example = "2025-12-31T23:59:59")
    private LocalDateTime expiredAt;
}
