package com.learningcrew.linkup.report.command.application.dto.response;

import com.learningcrew.linkup.report.command.domain.aggregate.PenaltyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "제재 처리 응답 DTO")
public class PenaltyResponse {

    @Schema(description = "제재 ID", example = "5001")
    private Long penaltyId;

    @Schema(description = "제재 대상 사용자 ID", example = "1024")
    private Integer userId;

    @Schema(description = "제재 유형 (POST, COMMENT, REVIEW)", example = "COMMENT")
    private PenaltyType penaltyType;

    @Schema(description = "제재 상태 ID (1: 요청, 2: 완료, 3: 거절)", example = "1")
    private Integer statusId;

    @Schema(description = "제재 대상 게시글 ID (POST일 경우)", example = "200")
    private Integer postId;

    @Schema(description = "제재 대상 댓글 ID (COMMENT일 경우)", example = "3456")
    private Long commentId;

    @Schema(description = "제재 대상 후기 ID (REVIEW일 경우)", example = "789")
    private Integer reviewId;

    @Schema(description = "처리 결과 메시지", example = "댓글 제재가 완료되었습니다.")
    private String resultMessage;
}
