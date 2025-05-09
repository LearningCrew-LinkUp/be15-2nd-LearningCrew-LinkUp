package com.learningcrew.linkupuser.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "모임 이력 조회 응답 DTO")
public class MeetingHistoryResponse {
    @Schema(description = "모임 ID")
    private Long meetingId;

    @Schema(description = "모임 제목")
    private String title;

    @Schema(description = "모임 일시")
    private String date;

    @Schema(description = "모임 장소")
    private String location;

    @Schema(description = "모임 상태 (COMPLETED, CANCELLED, UPCOMING)")
    private String status;
}
