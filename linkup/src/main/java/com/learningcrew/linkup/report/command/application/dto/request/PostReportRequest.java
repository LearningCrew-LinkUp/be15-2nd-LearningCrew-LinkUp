package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 신고 요청 DTO")
public class PostReportRequest {

    @NotNull(message = "신고자 ID는 필수입니다.")
    @Schema(description = "신고를 수행하는 회원의 ID", example = "101")
    private Integer reporterMemberId;

    @NotNull(message = "신고 대상자 ID는 필수입니다.")
    @Schema(description = "신고 대상이 되는 회원의 ID", example = "202")
    private Integer targetMemberId;

    @NotNull(message = "게시글 ID는 필수입니다.")
    @Schema(description = "신고 대상 게시글의 ID", example = "3456")
    private Integer postId;

    @NotNull(message = "신고 유형 ID는 필수입니다.")
    @Schema(description = "신고 유형 ID (예: 유해 행위는 1)", example = "1")
    private Byte reportTypeId;

    @NotNull(message = "신고 사유는 필수입니다.")
    @Size(min = 1, max = 1000)
    @Schema(description = "신고 사유", example = "해당 게시글은 욕설과 혐오 표현을 포함하고 있습니다.")
    private String reason;
}
