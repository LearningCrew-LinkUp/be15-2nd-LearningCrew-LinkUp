package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 또는 댓글 신고 등록 요청 DTO")
public class ReportRegisterRequest {

    @NotNull(message = "신고자 ID는 필수입니다.")
    @Schema(description = "신고자 회원 ID", example = "1001")
    private Integer reporterMemberId;

    @NotNull(message = "신고 대상자 ID는 필수입니다.")
    @Schema(description = "신고 대상자 회원 ID", example = "2002")
    private Integer targetMemberId;

    @Schema(description = "신고 대상 게시글 ID (게시글 신고일 경우)", example = "123")
    private Integer postId;

    @Schema(description = "신고 대상 댓글 ID (댓글 신고일 경우)", example = "456")
    private Long commentId;

    @NotNull(message = "신고 유형 ID는 필수입니다.")
    @Schema(description = "신고 유형 ID (예: 1 = 유해한 콘텐츠)", example = "1")
    private Byte reportTypeId;

    @NotNull(message = "신고 사유는 필수입니다.")
    @Size(min = 1, max = 1000, message = "신고 사유는 1자 이상 1000자 이하여야 합니다.")
    @Schema(description = "신고 사유", example = "게시글에 부적절한 내용이 포함되어 있습니다.")
    private String reason;
}
