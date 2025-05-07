package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "신고 유형 정보 DTO")
public record ReportTypeDTO(

        @Schema(description = "신고 유형 ID", example = "1")
        Byte id,

        @Schema(description = "신고 유형 이름", example = "유해하거나 위험한 행위")
        String name,

        @Schema(description = "신고 심각도 레벨 (높을수록 심각)", example = "100")
        Byte level

) {}
