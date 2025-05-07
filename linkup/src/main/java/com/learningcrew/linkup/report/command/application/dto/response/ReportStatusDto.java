package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "신고 상태 정보 DTO")
public class ReportStatusDto {

    @Schema(description = "신고 상태 ID", example = "2")
    private Integer statusId;

    @Schema(description = "신고 상태 설명", example = "처리 완료")
    private String description;
}
