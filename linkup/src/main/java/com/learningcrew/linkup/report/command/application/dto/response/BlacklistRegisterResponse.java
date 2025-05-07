package com.learningcrew.linkup.report.command.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "블랙리스트 등록 응답 DTO")
public class BlacklistRegisterResponse {

    @Schema(description = "블랙리스트에 등록된 회원의 ID", example = "1001")
    private Integer memberId;

    @Schema(description = "처리 결과 메시지", example = "블랙리스트 등록이 완료되었습니다.")
    private String message;
}
