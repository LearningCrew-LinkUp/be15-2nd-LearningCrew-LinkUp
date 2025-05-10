package com.learningcrew.linkup.report.command.domain.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<ReportHistory, Long> {
    Optional<ReportHistory> findByReporterIdAndTargetIdAndReportTypeIdAndPostIdAndCommentIdAndReason(
        Integer reporterId,
        Integer targetId,
        Byte reportTypeId,
        Integer postId,
        Long commentId,
        String reason
    );
}
