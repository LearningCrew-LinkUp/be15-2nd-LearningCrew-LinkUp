package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "신고 등록 응답 DTO")
public class ReportRegisterResponse {

    @Schema(description = "생성된 신고 ID", example = "5001")
    private Long reportId;

    @Schema(description = "신고자 회원 ID", example = "1001")
    private Integer reporterMemberId;

    @Schema(description = "신고 대상자 회원 ID", example = "2002")
    private Integer targetMemberId;

    @Schema(description = "신고 유형 ID", example = "1")
    private Byte reportTypeId;

    @Schema(description = "처리 결과 메시지", example = "신고가 성공적으로 등록되었습니다.")
    private String message;
}
