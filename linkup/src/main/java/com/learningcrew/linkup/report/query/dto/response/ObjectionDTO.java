package com.learningcrew.linkup.report.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectionDTO {

    @Schema(description = "이의제기 ID", example = "101")
    private Long objectionId;

    @Schema(description = "관련 제재 ID", example = "55")
    private Long penaltyId;

    @Schema(description = "이의 제기한 사용자 ID", example = "25")
    private Long memberId;

    @Schema(description = "사용자 이름", example = "사용자")
    private String userName;

    @Schema(description = "이의제기 상태 (PENDING, APPROVED, REJECTED)", example = "1")
    private Integer statusId;

    @Schema(description = "이의제기 사유", example = "부당한 제재로 인해 이의 제기합니다.")
    private String reason;

    @Schema(description = "이의제기 생성 일시", example = "2025-04-30T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "관련 제재 정보")
    private PenaltyInfoDTO penaltyInfo;
}
