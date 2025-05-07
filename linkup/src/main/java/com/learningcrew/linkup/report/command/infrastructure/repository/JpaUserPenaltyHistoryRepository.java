package com.learningcrew.linkup.report.command.infrastructure.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.UserPenaltyHistory;
import com.learningcrew.linkup.report.command.domain.repository.UserPenaltyHistoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface JpaUserPenaltyHistoryRepository extends JpaRepository<UserPenaltyHistory, Long>, UserPenaltyHistoryRepository {

    boolean existsByPostId(Integer postId);

    boolean existsByCommentId(Long commentId);

    boolean existsByReviewId(Integer reviewId);

    Optional<UserPenaltyHistory> findByPostIdAndStatusId(Integer postId, Integer statusId);

    Optional<UserPenaltyHistory> findByCommentIdAndStatusId(Long commentId, Integer statusId);

    Optional<UserPenaltyHistory> findByReviewIdAndStatusId(Integer reviewId, Integer statusId);

    // 선택적으로 기본 조회도 재사용 가능
    Optional<UserPenaltyHistory> findByPostId(Integer postId);  // statusId 조건 없이도
    Optional<UserPenaltyHistory> findByCommentId(Long commentId);
}
