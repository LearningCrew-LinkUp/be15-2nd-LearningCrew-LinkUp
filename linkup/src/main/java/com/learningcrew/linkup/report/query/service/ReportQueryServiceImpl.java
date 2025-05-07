package com.learningcrew.linkup.report.query.service;

import com.learningcrew.linkup.common.dto.Pagination;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.report.query.dto.request.ReportSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporteeSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReporterSearchRequest;
import com.learningcrew.linkup.report.query.dto.request.ReportTargetSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.ReportSimpleDTO;
import com.learningcrew.linkup.report.query.dto.response.*;
import com.learningcrew.linkup.report.query.mapper.ReportMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportMapper reportMapper;

    @Override
    public ReportListResponse getReports(ReportSearchRequest request) {
        // 페이징 설정 (size와 offset 기본값 처리)
        int size = request.getSize() > 0 ? request.getSize() : Integer.MAX_VALUE;
        int offset = request.getOffset() > 0 ? request.getOffset() : 0;

        // 신고 내역 조회
        List<ReportDTO> reports = reportMapper.selectReports(
                request.getReporterMemberId(),
                request.getTargetMemberId(),
                request.getReportTypeId(),
                request.getStatusId(),
                size,
                offset
        );

        // 전체 신고 내역 개수 조회
        long totalItems = reportMapper.countReports(
                request.getReporterMemberId(),
                request.getTargetMemberId(),
                request.getReportTypeId(),
                request.getStatusId()
        );

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalItems / size);
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalItems(totalItems)
                .totalPage(totalPages)
                .build();

        // 응답으로 ReportListResponse 반환
        return ReportListResponse.builder()
                .reports(reports)
                .pagination(pagination)
                .build();
    }

    // 신고 ID에 해당하는 신고 내역 상세 조회
    @Override
    public ReportDTO getReportById(Long reportId) {
        ReportDTO report = reportMapper.selectReportById(reportId);
        if (report == null) {
            throw new BusinessException(ErrorCode.REPORT_NOT_FOUND);
        }
        return report;
    }


    @Override
    public List<ReportTypeDTO> getReportTypes() {
        return reportMapper.selectAllReportTypes();
    }

    // 신고 대상별 목록 조회
    @Override
    public ReportTargetListResponse getReportTargetList(ReportTargetSearchRequest request) {
        List<ReportTargetDTO> targetList = reportMapper.selectReportTargetList(
                request.getTargetType(),
                request.getTargetId(),
                request.getIsActive(),
                request.getSize(),
                request.getOffset()
        );

        long totalItems = reportMapper.countReportTargetList(
                request.getTargetType(),
                request.getTargetId(),
                request.getIsActive()
        );

        int totalPages = (int) Math.ceil((double) totalItems / request.getSize());
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalItems(totalItems)
                .totalPage(totalPages)
                .build();

        return ReportTargetListResponse.builder()
                .targetList(targetList)
                .pagination(pagination)
                .build();
    }

    // 신고 대상 정보 조회 및 관련 목록 조회
    @Override
    public ReportTargetDetailResponse getReportTargetDetail(String targetType, Long targetId) {
        ReportTargetDTO targetInfo = reportMapper.selectReportTargetInfo(targetType, targetId);
        if (targetInfo == null) {
            throw new BusinessException(ErrorCode.REPORT_NOT_FOUND, "신고 대상 정보를 찾을 수 없습니다.");
        }

        List<ReportSimpleDTO> reportList = reportMapper.selectReportsByTarget(targetType, targetId);

        return ReportTargetDetailResponse.builder()
                .targetType(targetInfo.getTargetType())
                .targetId(targetInfo.getTargetId())
                .reportCount(targetInfo.getReportCount())
                .lastReportDate(targetInfo.getLastReportDate())
                .isActive(targetInfo.getIsActive())
                .reportList(reportList)
                .build();
    }



    @Override
    public ReportUserListResponse<ReportUserCountDTO> getReporterListByCount(ReporterSearchRequest request) {
        // 신고자 목록 조회 (누적 신고 횟수 기준)
        List<ReportUserCountDTO> reporterList = reportMapper.selectReporterListByCount(
                request.getReporterId(),
                request.getSize(),
                request.getOffset()
        );

        // 전체 신고자 수 조회
        long totalItems = reportMapper.countReporterListByCount(request.getReporterId());

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalItems / request.getSize());
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalItems(totalItems)
                .totalPage(totalPages)
                .build();

        // 응답으로 ReportUserListResponse 반환
        return ReportUserListResponse.<ReportUserCountDTO>builder()
                .userList(reporterList)
                .pagination(pagination)
                .build();
    }

    @Override
    public ReportUserListResponse<ReportUserScoreDTO> getReporteeListByScore(ReporteeSearchRequest request) {
        // 피신고자 목록 조회 (누적 신고 점수 기준)
        List<ReportUserScoreDTO> reporteeList = reportMapper.selectReporteeListByScore(
                request.getReporteeId(),
                request.getSize(),
                request.getOffset()
        );

        // 전체 피신고자 수 조회
        long totalItems = reportMapper.countReporteeListByScore(request.getReporteeId());

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalItems / request.getSize());
        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .totalItems(totalItems)
                .totalPage(totalPages)
                .build();

        // 응답으로 ReportUserListResponse 반환
        return ReportUserListResponse.<ReportUserScoreDTO>builder()
                .userList(reporteeList)
                .pagination(pagination)
                .build();
    }
}
