package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이의 제기 등록 응답 DTO")
public class ObjectionRegisterResponse {

    @Schema(description = "등록된 이의 제기 ID", example = "3001")
    private Long objectionId;

    @Schema(description = "해당 제재 ID", example = "2005")
    private Long penaltyId;

    @Schema(description = "이의 제기를 제출한 회원의 ID", example = "1002")
    private Integer memberId;

    @Schema(description = "이의 제기 상태 ID (1: 처리중, 2: 완료, 3: 거절)", example = "1")
    private Integer statusId;

    @Schema(description = "처리 결과 메시지", example = "이의 제기가 정상적으로 등록되었습니다.")
    private String message;
}
