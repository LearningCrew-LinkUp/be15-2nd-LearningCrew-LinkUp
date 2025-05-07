package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "제재 요청 DTO")
public class PenaltyRequest {

    @NotBlank(message = "제재 사유는 필수입니다.")
    @Schema(description = "제재 사유", example = "부적절한 콘텐츠 업로드로 인한 제재")
    private String reason;
}
