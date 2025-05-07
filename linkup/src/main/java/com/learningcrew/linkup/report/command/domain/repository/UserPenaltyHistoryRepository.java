package com.learningcrew.linkup.report.command.domain.repository;

import com.learningcrew.linkup.report.command.domain.aggregate.UserPenaltyHistory;

import java.util.Optional;

public interface UserPenaltyHistoryRepository {

    UserPenaltyHistory save(UserPenaltyHistory penalty);

    Optional<UserPenaltyHistory> findById(Long id);

    Optional<UserPenaltyHistory> findByPostId(Integer postId);
    Optional<UserPenaltyHistory> findByCommentId(Long commentId);
    Optional<UserPenaltyHistory> findByReviewIdAndStatusId(Integer reviewId, Integer statusId);

    // 중복 제재 이력 존재 여부 확인
    boolean existsByPostId(Integer postId);
    boolean existsByCommentId(Long commentId);
    boolean existsByReviewId(Integer reviewId);

    Optional<UserPenaltyHistory> findByReviewId(Integer reviewId);
}
