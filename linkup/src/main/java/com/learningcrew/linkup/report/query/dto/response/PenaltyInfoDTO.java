
package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyInfoDTO {

    @Schema(description = "제재 유형 (POST, COMMENT, REVIEW 중 하나)", example = "COMMENT")
    private String penaltyType;

    @Schema(description = "관련 게시글 ID (해당되는 경우만)", example = "31", nullable = true)
    private Long postId;

    @Schema(description = "관련 댓글 ID (해당되는 경우만)", example = "45", nullable = true)
    private Long commentId;

    @Schema(description = "관련 리뷰 ID (해당되는 경우만)", example = "78", nullable = true)
    private Long reviewId;

    @Schema(description = "제재 사유", example = "욕설이 포함된 댓글 작성")
    private String reason;

    @Schema(description = "제재 상태 (PENDING, APPROVED, REJECTED)", example = "1")
    private Integer statusId;

    @Schema(description = "제재 생성 일시", example = "2025-04-30T10:15:30")
    private LocalDateTime createdAt;
}
