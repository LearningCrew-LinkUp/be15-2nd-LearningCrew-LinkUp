package com.learningcrew.linkup.meeting.query.controller;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.meeting.query.dto.response.ReservedTimeResponse;
import com.learningcrew.linkup.meeting.query.service.MeetingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "모임 예약 시간 조회", description = "특정 장소, 날짜에 예약된 모임 시간대 확인 API")
public class MeetingReservationTimeController {

    private final MeetingQueryService meetingQueryService;

    @GetMapping("/reserved-times")
    @Operation(summary = "예약된 모임 시간대 조회", description = "선택한 장소와 날짜에 예약된 모임 시간대를 반환합니다.")
    public ResponseEntity<ApiResponse<List<ReservedTimeResponse>>> getReservedMeetingTimes(
            @RequestParam int placeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("🔍 예약 시간대 조회 요청: placeId={}, date={}", placeId, date);
        List<ReservedTimeResponse> result = meetingQueryService.getReservedTimes(placeId, date);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
