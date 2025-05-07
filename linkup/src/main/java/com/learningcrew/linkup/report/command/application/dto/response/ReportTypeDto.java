package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "신고 유형 정보 DTO")
public class ReportTypeDto {

    @Schema(description = "신고 유형 ID", example = "1")
    private Integer reportTypeId;

    @Schema(description = "신고 유형 이름", example = "comment")
    private String reportType;

    @Schema(description = "신고 심각도 레벨 (높을수록 심각)", example = "100")
    private Byte level;
}
