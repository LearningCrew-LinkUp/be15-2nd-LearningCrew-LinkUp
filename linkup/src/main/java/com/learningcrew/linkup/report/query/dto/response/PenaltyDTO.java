package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyDTO {

    @Schema(description = "제재 ID", example = "101")
    private Long penaltyId;

    @Schema(description = "사용자 ID", example = "25")
    private Long userId;

    @Schema(description = "사용자 이름", example = "사용자")
    private String userName;

    @Schema(description = "제재 유형 (POST, COMMENT, REVIEW 중 하나)", example = "COMMENT")
    private String penaltyType;

    @Schema(description = "제재 사유", example = "욕설이 포함된 댓글 작성")
    private String reason;

    @Schema(description = "제재 생성 날짜", example = "2025-04-30T10:20:00")
    private String createdAt;

    @Schema(description = "제재 상태 (PENDING, APPROVED, REJECTED)", example = "1")
    private Integer statusId;

    @Schema(description = "관련 게시글 ID (해당되는 경우만)", example = "301", nullable = true)
    private Long postId;

    @Schema(description = "관련 댓글 ID (해당되는 경우만)", example = "456", nullable = true)
    private Long commentId;

    @Schema(description = "관련 리뷰 ID (해당되는 경우만)", example = "789", nullable = true)
    private Long reviewId;
}
