package com.learningcrew.linkup.report.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltySearchRequest {

    @Schema(description = "페이지 번호", example = "1", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지당 항목 수", example = "10", defaultValue = "10")
    private int size = 10;

    @Schema(description = "제재 유형 (예: POST, COMMENT, REVIEW)", example = "COMMENT")
    private String penaltyType;

    @Schema(description = "사용자 ID (제재 대상자)", example = "42")
    private Long userId;

    @Schema(description = "제재 상태 ID (예: 1=처리중, 2=처리완료, 3=제재철회)", example = "1")
    private Integer statusId;

    public int getOffset() {
        return (page - 1) * size;
    }
}
