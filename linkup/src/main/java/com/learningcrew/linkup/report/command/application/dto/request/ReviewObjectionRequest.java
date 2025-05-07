package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "장소 후기 제재에 대한 이의 제기 요청 DTO")
public class ReviewObjectionRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    @Schema(description = "이의를 제기하는 회원의 ID", example = "1003")
    private Integer memberId;

    @NotBlank(message = "이의 제기 사유는 필수입니다.")
    @Schema(description = "이의 제기 사유", example = "해당 후기는 사실에 근거한 평가이며 규정에 어긋나지 않습니다.")
    private String reason;
}
