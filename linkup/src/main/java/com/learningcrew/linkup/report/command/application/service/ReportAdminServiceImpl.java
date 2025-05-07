package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.report.command.application.dto.request.HandleReportRequest;
import com.learningcrew.linkup.report.command.application.dto.response.ReportHandleResponse;
import com.learningcrew.linkup.report.command.domain.aggregate.ReportHistory;
import com.learningcrew.linkup.report.command.domain.aggregate.UserPenaltyHistory;
import com.learningcrew.linkup.report.command.domain.repository.ReportHistoryRepository;
import com.learningcrew.linkup.report.command.domain.repository.UserPenaltyHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportAdminServiceImpl implements ReportAdminService {

    private final ReportHistoryRepository reportRepository;
    private final UserPenaltyHistoryRepository userPenaltyHistoryRepository;

    // 허위 신고 처리 (상태만 변경)
    @Transactional
    @Override
    public ReportHandleResponse markAsFalseReport(Long reportId) {
        ReportHistory report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 신고가 존재하지 않습니다: " + reportId));

        report.updateStatus(3); // REJECTED
        reportRepository.save(report);

        return ReportHandleResponse.builder()
                .reportId(report.getId())
                .statusId(report.getStatusId())
                .message("허위 신고로 처리되었습니다.")
                .build();
    }

    // 신고 처리 + 제재 등록
    @Transactional
    @Override
    public ReportHandleResponse completeReportAndPenalize(Long reportId, HandleReportRequest request) {
        ReportHistory report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 신고가 존재하지 않습니다: " + reportId));

        // 신고 처리 완료 상태로 변경
        report.updateStatus(2); // ACCEPTED
        reportRepository.save(report);

        // 제재 이력 등록
        UserPenaltyHistory penalty = UserPenaltyHistory.builder()
                .userId(report.getTargetId())
                .postId(report.getPostId())
                .commentId(report.getCommentId())
                .penaltyType(request.getPenaltyType())
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .statusId(2)
                .build();

        userPenaltyHistoryRepository.save(penalty);

        return ReportHandleResponse.builder()
                .reportId(report.getId())
                .statusId(report.getStatusId())
                .message("신고가 처리되고 제재가 등록되었습니다.")
                .build();
    }
}
