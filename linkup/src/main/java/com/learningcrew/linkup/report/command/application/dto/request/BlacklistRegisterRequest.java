package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "블랙리스트 등록 요청 DTO")
public class BlacklistRegisterRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    @Schema(description = "블랙리스트로 등록할 회원의 ID", example = "1024")
    private Integer memberId;

    @NotBlank(message = "블랙리스트 등록 사유는 필수입니다.")
    @Schema(description = "블랙리스트 등록 사유", example = "반복적인 허위 신고로 인해 블랙리스트에 등록합니다.")
    private String reason;
}
