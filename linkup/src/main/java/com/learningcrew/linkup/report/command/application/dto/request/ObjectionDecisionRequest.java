package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "이의 제기 처리 요청 DTO")
public class ObjectionDecisionRequest {

    @NotNull(message = "이의 제기 ID는 필수입니다.")
    @Schema(description = "처리할 이의 제기의 ID", example = "1234")
    private Long objectionId;

    @NotNull(message = "처리 상태 ID는 필수입니다.")
    @Schema(description = "처리 상태 ID (2: 승인, 3: 거절)", example = "2")
    private Integer statusId;
}
