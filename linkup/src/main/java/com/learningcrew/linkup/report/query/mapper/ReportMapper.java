package com.learningcrew.linkup.report.query.mapper;

import com.learningcrew.linkup.report.query.dto.request.ReportSearchRequest;
import com.learningcrew.linkup.report.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {

    // 신고 내역 조회 (페이징 및 필터링)
    List<ReportDTO> selectReports(
            @Param("reporterMemberId") Long reporterMemberId,
            @Param("targetMemberId") Long targetMemberId,
            @Param("reportTypeId") Long reportTypeId,
            @Param("statusId") Long statusId,
            @Param("size") int size,
            @Param("offset") int offset
    );

    // 신고 내역 전체 건수 조회
    long countReports(
            @Param("reporterMemberId") Long reporterMemberId,
            @Param("targetMemberId") Long targetMemberId,
            @Param("reportTypeId") Long reportTypeId,
            @Param("statusId") Long statusId
    );

    // 신고 상세 조회
    ReportDTO selectReportById(Long reportId);

    // 신고 대상별 목록 조회
    List<ReportTargetDTO> selectReportTargetList(@Param("targetType") String targetType,
                                                 @Param("targetId") Long targetId,
                                                 @Param("isActive") String isActive,
                                                 @Param("size") int size,
                                                 @Param("offset") int offset);

    // 신고 대상별 상세 조회
    long countReportTargetList(@Param("targetType") String targetType,
                               @Param("targetId") Long targetId,
                               @Param("isActive") String isActive);


    // 대상 기본 정보 + 신고 건수 조회
    ReportTargetDTO selectReportTargetInfo(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    // 대상에 대해 접수된 신고 리스트 조회
    List<ReportSimpleDTO> selectReportsByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    // 신고자 목록 조회 (누적 신고 횟수 기준)
    List<ReportUserCountDTO> selectReporterListByCount(
            @Param("reporterId") Long reporterId,
            @Param("size") int size,
            @Param("offset") int offset
    );

    // 신고자 목록 전체 건수 조회
    long countReporterListByCount(@Param("reporterId") Long reporterId);

    // 피신고자 목록 조회 (신고 당한 횟수와 신고 점수 포함)
    List<ReportUserScoreDTO> selectReporteeListByScore(
            @Param("reporteeId") Long reporteeId,
            @Param("size") int size,
            @Param("offset") int offset
    );

    // 피신고자 목록 전체 건수 조회
    long countReporteeListByScore(@Param("reporteeId") Long reporteeId);

    // 신고 종류 조회
    List<ReportTypeDTO> selectAllReportTypes();
}
