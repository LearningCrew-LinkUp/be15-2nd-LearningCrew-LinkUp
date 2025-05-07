package com.learningcrew.linkup.report.query.controller;

import com.learningcrew.linkup.report.query.dto.request.ReportSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReportTargetSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporteeSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporterSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.*;
import com.learningcrew.linkup.report.query.dto.response.ReportTypeDTO;
import com.learningcrew.linkup.report.query.service.ReportQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Tag(name = "신고 조회", description = "관리자 전용 신고 조회 API")
public class ReportQueryController {

    private final ReportQueryService reportQueryService;

    @GetMapping
    @Operation(summary = "신고 전체 내역 조회", description = "상태별 필터링을 포함한 신고 내역 조회")
    public ResponseEntity<ReportListResponse> getReports(
            @ParameterObject @ModelAttribute ReportSearchRequest request) {
        // 신고 내역 조회 요청을 서비스로 전달하고 응답 반환
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "신고 상세 조회", description = "신고 ID를 기준으로 신고 내역을 조회한다.")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long reportId) {
        // 신고 ID에 해당하는 신고 내역 상세 조회 요청
        return ResponseEntity.ok(reportQueryService.getReportById(reportId));
    }

    @GetMapping("/types")
    @Operation(summary = "신고 유형 목록 조회", description = "신고 가능한 유형과 심각도 레벨 목록을 조회한다.")
    public ResponseEntity<List<ReportTypeDTO>> getReportTypes() {
        return ResponseEntity.ok(reportQueryService.getReportTypes());
    }


    @GetMapping("/target")
    @Operation(summary = "신고 대상별 목록 조회", description = "대상 유형, 대상 ID, 활성화 상태별로 신고 대상 목록을 조회한다.")
    public ResponseEntity<ReportTargetListResponse> getReportTargetList(
            @ParameterObject @ModelAttribute ReportTargetSearchRequest request) {
        return ResponseEntity.ok(reportQueryService.getReportTargetList(request));
    }

    @GetMapping("/target/{targetType}/{targetId}")
    @Operation(summary = "신고 대상별 상세 조회", description = "특정 대상 ID와 유형에 따른 신고 상세 내역을 조회한다.")
    public ResponseEntity<ReportTargetDetailResponse> getReportTargetDetail(
            @PathVariable String targetType,
            @PathVariable Long targetId) {
        return ResponseEntity.ok(reportQueryService.getReportTargetDetail(targetType, targetId));
    }



    @GetMapping("/reporter-user")
    @Operation(summary = "누적 신고 횟수별 신고자 목록 조회", description = "신고자 ID별로 필터링 가능하며, 최근 신고일을 포함한 신고자 목록을 조회한다.")
    public ResponseEntity<ReportUserListResponse<ReportUserCountDTO>> getReporterListByCount(
            @ParameterObject ReporterSearchRequest request) {
        // 신고자 목록을 신고 횟수 기준으로 조회하는 서비스 호출
        return ResponseEntity.ok(reportQueryService.getReporterListByCount(request));
    }

    @GetMapping("/reportee-user")
    @Operation(summary = "누적 신고 점수별 피신고자 목록 조회", description = "피신고자 ID별로 필터링 가능하며, 신고 당한 횟수를 포함한 피신고자 목록을 조회한다.")
    public ResponseEntity<ReportUserListResponse<ReportUserScoreDTO>> getReporteeListByScore(
            @ParameterObject ReporteeSearchRequest request) {
        // 피신고자 목록을 신고 점수 기준으로 조회하는 서비스 호출
        return ResponseEntity.ok(reportQueryService.getReporteeListByScore(request));
    }

    @GetMapping("/reporter-user/{reporterId}")
    @Operation(summary = "신고자별 신고 내역 조회", description = "관리자가 신고한 특정 신고자에 따른 신고 내역을 조회한다.")
    public ResponseEntity<ReportListResponse> getReportsByReporter(@PathVariable Long reporterId,
                                                                   @Parameter(hidden = true) ReportSearchRequest request) {
        // 특정 신고자 ID를 사용하여 신고 내역 조회
        request.setReporterMemberId(reporterId);
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }

    @GetMapping("/reportee-user/{reporteeId}")
    @Operation(summary = "피신고자별 신고 내역 조회", description = "관리자가 신고된 특정 피신고자에 따른 신고 내역을 조회한다.")
    public ResponseEntity<ReportListResponse> getReportsByReportee(@PathVariable Long reporteeId,
                                                                   @Parameter(hidden = true) ReportSearchRequest request) {
        // 특정 피신고자 ID를 사용하여 신고 내역 조회
        request.setTargetMemberId(reporteeId);
        return ResponseEntity.ok(reportQueryService.getReports(request));
    }
}
