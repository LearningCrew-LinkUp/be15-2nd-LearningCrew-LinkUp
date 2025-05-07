package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    @Schema(description = "신고 ID", example = "101")
    private Long reportId;

    @Schema(description = "신고자 사용자 ID", example = "50")
    private Long reporterMemberId;

    @Schema(description = "신고자 이름", example = "사용자")
    private String reporterName;

    @Schema(description = "피신고자 사용자 ID", example = "65")
    private Long targetMemberId;

    @Schema(description = "피신고자 이름", example = "김철수")
    private String targetName;

    @Schema(description = "신고 유형 ID", example = "2")
    private Byte reportTypeId;

    @Schema(description = "신고 유형 이름 (예: 스팸, 욕설)", example = "욕설")
    private String reportType;

    @Schema(description = "신고 상태 (PENDING, APPROVED, REJECTED)", example = "PENDING")
    private Integer statusId;

    @Schema(description = "신고 사유", example = "게시글에 부적절한 내용이 포함됨")
    private String reason;

    @Schema(description = "관련 게시글 ID (해당되는 경우)", example = "31", nullable = true)
    private Long postId;

    @Schema(description = "관련 댓글 ID (해당되는 경우)", example = "46", nullable = true)
    private Long commentId;

    @Schema(description = "신고 생성 일시", example = "2025-04-30T10:30:00")
    private LocalDateTime createdAt;
}
