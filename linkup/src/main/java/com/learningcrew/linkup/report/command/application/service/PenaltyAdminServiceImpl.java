package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.community.command.domain.aggregate.PostComment;
import com.learningcrew.linkup.community.command.domain.repository.PostCommentRepository;
import com.learningcrew.linkup.community.command.domain.repository.PostRepository;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.place.command.domain.repository.PlaceReviewRepository;
import com.learningcrew.linkup.report.command.application.dto.request.PenaltyRequest;
import com.learningcrew.linkup.report.command.application.dto.response.PenaltyResponse;
import com.learningcrew.linkup.report.command.domain.aggregate.PenaltyType;
import com.learningcrew.linkup.report.command.domain.aggregate.UserPenaltyHistory;
import com.learningcrew.linkup.report.command.domain.repository.ReportHistoryRepository;
import com.learningcrew.linkup.report.command.domain.repository.UserPenaltyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PenaltyAdminServiceImpl implements PenaltyAdminService {

    private final UserPenaltyHistoryRepository penaltyRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final PlaceReviewRepository reviewRepository;
    private final ReportHistoryRepository reportRepository;

    @Override
    public PenaltyResponse penalizePost(Integer postId, PenaltyRequest request) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (penaltyRepository.existsByPostId(postId)) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_EXISTS);
        }

        post.setIsDelete("Y");
        post.setPostDeletedAt(LocalDateTime.now());
        reportRepository.updateStatusByPostId(postId);

        var penalty = savePenalty(
                PenaltyType.POST,
                post.getUserId(),
                request.getReason(),
                2,
                postId,
                null,
                null
        );

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(PenaltyType.POST)
                .postId(postId)
                .statusId(penalty.getStatusId())
                .resultMessage("게시글 제재 완료")
                .build();
    }

    @Override
    public PenaltyResponse penalizeComment(Long commentId, PenaltyRequest request) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (penaltyRepository.existsByCommentId(commentId)) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_EXISTS);
        }

        comment.setIsDeleted("Y");
        comment.setDeletedAt(LocalDateTime.now());

        reportRepository.updateStatusByCommentId(commentId, 2, request.getReason());

        var penalty = savePenalty(
                PenaltyType.COMMENT,
                comment.getUserId(),
                request.getReason(),
                2,
                null,
                commentId,
                null
        );

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(PenaltyType.COMMENT)
                .commentId(commentId)
                .statusId(penalty.getStatusId())
                .resultMessage("댓글 제재 완료")
                .build();
    }

    @Override
    public PenaltyResponse penalizeReview(Integer reviewId, PenaltyRequest request) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (penaltyRepository.existsByReviewId(reviewId)) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_EXISTS);
        }

        review.setStatusId(1); // 검토중

        var penalty = savePenalty(
                PenaltyType.REVIEW,
                review.getMemberId(),
                request.getReason(),
                1,
                null,
                null,
                reviewId
        );

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(PenaltyType.REVIEW)
                .reviewId(reviewId)
                .statusId(penalty.getStatusId())
                .resultMessage("장소 후기 제재 요청 완료")
                .build();
    }

    @Override
    public PenaltyResponse confirmReviewPenalty(Integer reviewId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (review.getStatusId() == 3) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_CONFIRMED);
        }

        review.setStatusId(2); // 제재 확정

        penaltyRepository.findByReviewId(reviewId)
                .ifPresent(p -> p.setStatusId(2)); // 제재 확정 처리

        return PenaltyResponse.builder()
                .reviewId(reviewId)
                .resultMessage("장소 후기 제재 확정 완료")
                .build();
    }

    @Override
    public PenaltyResponse cancelPenalty(Long penaltyId) {
        var penalty = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENALTY_NOT_FOUND));

        Integer statusId = penalty.getStatusId();
        if (statusId != null && statusId == 3) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_CANCELED);
        }

        penalty.setStatusId(3);

        switch (penalty.getPenaltyType()) {
            case POST -> {
                var post = postRepository.findById(penalty.getPostId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
                post.setIsDeleted("N");
            }
            case COMMENT -> {
                var comment = commentRepository.findById(penalty.getCommentId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
                comment.setIsDeleted("N");
            }
            case REVIEW -> {
                var review = reviewRepository.findById(penalty.getReviewId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
                review.setStatusId(2); // 복구
            }
        }

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(penalty.getPenaltyType())
                .statusId(penalty.getStatusId())
                .resultMessage("제재 철회 완료")
                .build();
    }

    private UserPenaltyHistory savePenalty(
            PenaltyType type,
            Integer userId,
            String reason,
            Integer statusId,
            Integer postId,
            Long commentId,
            Integer reviewId
    ) {
        return penaltyRepository.save(UserPenaltyHistory.builder()
                .userId(userId)
                .penaltyType(type)
                .reason(reason)
                .statusId(statusId)
                .postId(postId)
                .commentId(commentId)
                .reviewId(reviewId)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
