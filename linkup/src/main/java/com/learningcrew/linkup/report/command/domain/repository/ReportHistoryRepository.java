package com.learningcrew.linkup.report.command.domain.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.ReportHistory;
import com.learningcrew.linkup.report.command.domain.aggregate.UserPenaltyHistory;

import java.util.Optional;

public interface ReportHistoryRepository {

    ReportHistory save(ReportHistory report);

    Optional<ReportHistory> findById(Long id);

    Optional<ReportHistory> findByReporterIdAndTargetIdAndReportTypeIdAndPostIdAndCommentId(
            Integer reporterId,
            Integer targetId,
            Byte reportTypeId,
            Integer postId,
            Long commentId
    );

    void updateStatusByPostId(Integer postId);

    void markAllReportsHandledByMemberId(Integer memberId);

    void updateStatusByCommentId(Long commentId, Integer statusId, String reason);

}
