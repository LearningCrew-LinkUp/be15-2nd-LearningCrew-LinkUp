package com.learningcrew.linkup.report.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 제재에 대한 이의 제기 요청 DTO")
public class CommentObjectionRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    @Schema(description = "이의를 제기하는 회원의 ID", example = "2048")
    private Integer memberId;

    @NotBlank(message = "이의 제기 사유는 필수입니다.")
    @Schema(description = "이의 제기 사유", example = "이 댓글은 특정인을 비방하지 않았으며, 오해가 있었습니다.")
    private String reason;
}
