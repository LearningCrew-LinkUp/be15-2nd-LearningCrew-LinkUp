package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.report.query.dto.request.ReportSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporteeSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporterSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReportTargetSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.*;

import java.util.List;

public interface ReportQueryService {

    // 신고 내역 조회 (페이징 및 필터링 처리)
    ReportListResponse getReports(ReportSearchRequest request);

    // 신고 상세 조회
    ReportDTO getReportById(Long reportId);

    // 신고 대상 별 목록 조회
    ReportTargetListResponse getReportTargetList(ReportTargetSearchRequest request);

    // 신고 대상 상세 조회
    ReportTargetDetailResponse getReportTargetDetail(String targetType, Long targetId);

    // 신고자 목록 조회 (누적 신고 횟수 기준)
    ReportUserListResponse<ReportUserCountDTO> getReporterListByCount(ReporterSearchRequest request);

    // 피신고자 목록 조회 (누적 신고 점수 기준)
    ReportUserListResponse<ReportUserScoreDTO> getReporteeListByScore(ReporteeSearchRequest request);

    List<ReportTypeDTO> getReportTypes();
}
