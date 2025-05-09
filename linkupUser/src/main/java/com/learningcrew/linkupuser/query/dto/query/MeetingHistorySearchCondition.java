package com.learningcrew.linkupuser.query.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "모임 이력 검색 조건")
public class MeetingHistorySearchCondition {

    @Schema(description = "모임 상태 (PENDING)")
    private String status;
}

