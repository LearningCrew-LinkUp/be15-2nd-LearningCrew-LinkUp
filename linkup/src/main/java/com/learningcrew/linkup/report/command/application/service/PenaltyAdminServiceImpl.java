package com.learningcrew.linkup.report.command.application.service;

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
    // private final PostRepository postRepository; // TODO: postRepository 구현 후 주석 해제
    // private final CommentRepository commentRepository; // TODO: commentRepository 구현 후 주석 해제
    private final PlaceReviewRepository reviewRepository;
    private final ReportHistoryRepository reportRepository;

    @Override
    public PenaltyResponse penalizePost(Integer postId, PenaltyRequest request) {
        // TODO: postRepository 사용 가능 시 아래 주석 해제
        /*
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (penaltyRepository.existsByPostId(postId)) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_EXISTS);
        }

        post.setPostIsDeleted("Y");
        post.setPostDeletedAt(LocalDateTime.now());
        reportRepository.updateStatusByPostId(postId);

        var penalty = penaltyRepository.save(UserPenaltyHistory.builder()
                .userId(post.getUserId())
                .postId(postId)
                .penaltyType(PenaltyType.POST)
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .isActive("Y")
                .build());

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(PenaltyType.POST)
                .postId(postId)
                .resultMessage("게시글 제재 완료")
                .build();
        */

        // 임시 응답 처리
        return PenaltyResponse.builder()
                .postId(postId)
                .resultMessage("게시글 제재 (임시 처리)")
                .build();
    }

    @Override
    public PenaltyResponse penalizeComment(Long commentId, PenaltyRequest request) {
        // TODO: commentRepository 사용 가능 시 아래 주석 해제
        /*
        var comment = commentRepository.findById(BigInteger.valueOf(commentId))
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (penaltyRepository.existsByCommentId(commentId)) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_EXISTS);
        }

        comment.setPostCommentIsDeleted("Y");
        comment.setPostCommentDeletedAt(LocalDateTime.now());
        reportRepository.updateStatusByCommentId(commentId);

        // 신고 처리 상태 업데이트
        reportRepository.updateStatusByCommentId(BigInteger.valueOf(commentId));

        // 제재 이력 저장
        var penalty = penaltyRepository.save(UserPenaltyHistory.builder()
                .userId(comment.getUserId())
                .commentId(BigInteger.valueOf(commentId))
                .penaltyType(PenaltyType.COMMENT)
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .isActive("Y")
                .build());

        // 응답 생성
        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(PenaltyType.COMMENT)
                .commentId(commentId)
                .resultMessage("댓글 제재 완료")
                .build();
        */

        // 임시 응답 처리
        return PenaltyResponse.builder()
                .commentId(commentId)
                .resultMessage("댓글 제재 (임시 처리)")
                .build();
    }

    @Override
    public PenaltyResponse penalizeReview(Integer reviewId, PenaltyRequest request) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (penaltyRepository.existsByReviewId(reviewId)) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_EXISTS);
        }

        review.setStatusId(1); // 상태: 검토중

        var penalty = penaltyRepository.save(UserPenaltyHistory.builder()
                .userId(review.getMemberId())
                .reviewId(reviewId)
                .penaltyType(PenaltyType.REVIEW)
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .isActive("Y")
                .build());

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(PenaltyType.REVIEW)
                .reviewId(reviewId)
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

        review.setStatusId(3); // 상태: 제재 확정 (삭제 처리)

        return PenaltyResponse.builder()
                .reviewId(reviewId)
                .resultMessage("장소 후기 제재 확정 완료")
                .build();
    }

    @Override
    public PenaltyResponse cancelPenalty(Long penaltyId) {
        var penalty = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PENALTY_NOT_FOUND));

        if ("N".equals(penalty.getIsActive())) {
            throw new BusinessException(ErrorCode.PENALTY_ALREADY_CANCELED);
        }

        penalty.setIsActive("N");

        switch (penalty.getPenaltyType()) {
            case POST -> {
                // TODO: postRepository 사용 가능 시 복구
                /*
                var post = postRepository.findById(penalty.getPostId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
                post.setPostIsDeleted("N");
                */
            }
            case COMMENT -> {
                // TODO: commentRepository 사용 가능 시 복구
                /*
                var comment = commentRepository.findById(penalty.getCommentId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
                comment.setPostCommentIsDeleted("N");
                */
            }
            case REVIEW -> {
                var review = reviewRepository.findById(penalty.getReviewId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
                review.setStatusId(2); // 복구 상태
            }
        }

        return PenaltyResponse.builder()
                .penaltyId(penalty.getId())
                .userId(penalty.getUserId())
                .penaltyType(penalty.getPenaltyType())
                .resultMessage("제재 철회 완료")
                .build();
    }
}
